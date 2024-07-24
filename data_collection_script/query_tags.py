import json
from datetime import datetime
import requests
import traceback
import time
import random
import pandas as pd
import os
from multiprocessing import Pool

current_token = ''

root_path = ''


def load_data():
    with open(f'{root_path}/data/github_repositories_repo.csv', 'r') as f:
        data = pd.read_csv(f)
    return data['full_name'].tolist()

def print_logs(message):
    t = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    print((t + "    " + message))


def post_query(repo_name, token):
    try:
        url = 'https://api.github.com/repos/' + repo_name + '/tags?per_page=100'
        response_result = requests.get(url, headers={"Authorization": "token " + token})
        if 'messgae' in response_result.json():
            print_logs(f"token {token} is invalid")
            return None
        return response_result.json()
    except:
        print(traceback.format_exc())
    return None


def multi_process_collect_tags(r_name):
    tokens = [
        {"name": "1", "token": "token1"},
        {"name": "2", "token": "token2"},
        {"name": "3", "token": "token3"},
        {"name": "4", "token": "token4"}
    ]
    t = random.randint(0, len(tokens) - 1)
    current_token = tokens[t]['token']
    repo_name = str(r_name).replace('/', '__')
    if os.path.exists(f"{root_path}/data/tags/{repo_name}.json"):
        return
    print_logs(f"Processing repo: {repo_name} with token {t}")
    with open(f"{root_path}/data/tags/{repo_name}.json", 'w') as w:
        w.write(json.dumps({
        'repo': r_name,
        'tags': post_query(r_name, current_token)
    }))
        

def remove_wrong_tags(name_list):
    count = 0
    for name in name_list:
        repo_name = str(name).replace('/', '__')
        if os.path.exists(f"{root_path}/data/tags/{repo_name}.json"):
            try:
                with open(f"{root_path}/data/tags/{repo_name}.json", 'r') as r:
                    data = json.load(r)
                if data['tags'] is None or 'message' in data['tags']:
                    print_logs(f"remove {repo_name}")
                    os.remove(f"{root_path}/data/tags/{repo_name}.json")
                    count += 1
                    continue
            except:
                os.remove(f"{root_path}/data/tags/{repo_name}.json")
                count += 1
                continue
    print_logs(f"remove {count} files")



if __name__ == '__main__':
    name_list = load_data()
    batch_size = 10000
    for i in range(0, len(name_list), batch_size):
        with Pool(20) as p:
            p.map(multi_process_collect_tags, name_list[i:i+batch_size])
        # time.sleep(60 * 10)
    remove_wrong_tags(name_list)