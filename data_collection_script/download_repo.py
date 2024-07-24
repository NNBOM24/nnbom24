import json
import os
import pandas as pd
from datetime import datetime
import random
from multiprocessing import Pool, Value, Lock
import time


# Initialize count as a shared memory Value
count = Value('i', 0)
# Introduce a lock for updating the count
count_lock = Lock()

root_path = ''
tokens = [
        {"name": "1", "token": " "},
        {"name": "2", "token": " "},
        {"name": "3", "token": " "},
        {"name": "4", "token": " "}
    ]

def print_logs(message):
    t = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    print((t + "    " + message))
    

count = Value('i', 0)
count_lock = Lock()


def load_name_list(i):
    print_logs(f"loading {i}-batch.csv")
    data = pd.read_csv(os.path.join(root_path, f'tags_add/{i}-batch.csv'))
    return data


def view_tags(row):
    repo_name = row['repo_name'].replace("/", "__")
    tag_name = row['tag_name']
    url = row['url']
    
    repo_path = os.path.join('D:/repos', repo_name)
    if not os.path.exists(repo_path):
        os.mkdir(repo_path)
    
    target_path = os.path.join(repo_path, f"{tag_name}.tar.gz")
    if not os.path.exists(target_path):
        t = tokens[random.randint(0, len(tokens) - 1)]['token']
        os.system(f'wget -q --auth-no-challenge --header=\'Authorization: token {t} \' -O {target_path} {url}')
    
    with count_lock:
        count.value += 1
        print_logs(f"downloaded: {count.value} {repo_name} {tag_name}")

if __name__ == '__main__':
    cnt = 0
    for i in range(0,35):
        df = load_name_list(i * 2000)
        rows = [row for _, row in df.iterrows()]
        with Pool(20) as p:
            p.map(view_tags, rows)
        print_logs(f"finished {i}-batch.csv")
    print(cnt)

