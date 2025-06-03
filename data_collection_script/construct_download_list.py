import json
import os
import pandas as pd
from datetime import datetime
import requests
import random
import time

 
root_path = 'C:/NNBOM/nnbom24/data_collection_script'
tokens = [
        {"name": "1", "token": "xxx"},
        {"name": "2", "token": "xxx"},
        {"name": "3", "token": "xxx"},
        {"name": "4", "token": "xxx"},
    ]

# repo list
df = pd.read_csv(root_path + '/data_new/github_repositories_data_new.csv')

default_branch = {}
for i in range(len(df)):
    default_branch[df['full_name'][i]] = df['default_branch'][i]


def print_logs(message):
    t = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    print((t + "    " + message))


def load_data():
    return df['full_name'].tolist()


def query_default_branch(repo_name):
    if repo_name not in default_branch:
        print_logs(f"===========no default branch: {repo_name}")
        return None
    try:
        t = tokens[random.randint(0, len(tokens) - 1)]['token']
        response_result = requests.get(
            f"https://api.github.com/repos/{repo_name}/commits?sha={default_branch[repo_name]}",
            headers={"Authorization": "token " + t}
        )
        commits = response_result.json()
        commit_num = commits[0]['sha']
        commit_date = commits[0]['commit']['committer']['date']
        if "message" in commits:
            print(commits['message'])
            print_logs(f"===========no commits: {repo_name}")
            return None
        return {
            "repo_name": repo_name,
            "tag_name": str(default_branch[repo_name]),
            "commit_sha": commit_num,
            "url": f"https://github.com/{repo_name}/archive/{commit_num}.tar.gz",
        }
    except Exception as e:
        print(e)
        print_logs(f"============no commits: {repo_name}")
    return None


def construct_download_list(name_list):
    batch_size = 2000
    for i in range(0, len(name_list), batch_size):
        # if i < 154000:
        #     continue
        print_logs(f"start: {i}")
        result = []  # list of dicts
        file_name = f"{root_path}/data_new/tags_add/{str(i)}-batch.csv"
        processed_list = []
        if os.path.exists(file_name):
            print_logs(f"file exists: {i}")
            processed = pd.read_csv(file_name)
            processed_list = processed['repo_name'].tolist()
        end_idx = min(i + batch_size, len(name_list))
        for j in range(i, end_idx):
            try:
                name = str(name_list[j]).replace('/', '__')
                if name_list[j] in processed_list:
                    continue
                if os.path.exists(f"{root_path}/data_new/tags/{name}.json"):
                    with open(f"{root_path}/data_new/tags/{name}.json", 'r') as r:
                        content = json.loads(r.read())
                    if content['tags'] is None or len(content['tags']) == 0:
                        value = query_default_branch(name_list[j])
                        if value is not None:
                            result.append(value)
                        else:
                            print_logs(f"Warning: query_default_branch returned None for {name_list[j]}")
                    else:
                        if 'message' in content['tags']:
                            continue
                        for tag in content['tags']:
                            # print_logs(f"================start: {j} {tag['name']} {len(content['tags'])}")
                            result.append({
                                "repo_name": name_list[j],
                                "tag_name": tag['name'],
                                "commit_sha": tag['commit']['sha'],
                                "url": tag['tarball_url']
                            })
                    # print_logs(f"start saving: {i}")
                else:
                    print_logs(f"Warning: {name_list[j]} not exists")
            except Exception as e:
                print(e)
                print(content)
                continue
        df = pd.DataFrame(result)
        # add the new data to the existing data
        if os.path.exists(file_name):
            if len(df) == 0:
                continue
            print_logs(f"added missing data: {i}")
            processed = pd.read_csv(file_name)
            df = pd.concat([processed, df])
            df.to_csv(file_name, index=False)
        else:
            print_logs(f"created new file: {i}")
            df.to_csv(file_name, index=False)
            #time.sleep(50) 
        


if __name__ == '__main__':
    name_list = load_data()
    construct_download_list(name_list)
        