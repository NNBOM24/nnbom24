import time
import requests
import csv
from datetime import datetime, timedelta

url = "https://api.github.com/search/repositories"
params_base = {
    'q': 'language:python pytorch',
    'per_page': 100,
    'sort': 'stars',
    'order': 'desc',
}

# Add your GitHub personal access token here
headers = {
    'Authorization': 'token xxx',
}

all_data = []
csv_filename = 'github_repositories_repo_new2.csv'
# Save to CSV, append to the existing file
def append_to_csv(time):
    with open(csv_filename, 'a', newline='', encoding='utf-8') as csv_file:
        fieldnames = ['full_name', 'default_branch', 'forks', 'watchers', 'topics', 'created_at']
        writer = csv.DictWriter(csv_file, fieldnames=fieldnames)

        # If the file is empty, write the header
        if csv_file.tell() == 0:
            writer.writeheader()

        # Write data
        writer.writerows(all_data)
    all_data.clear()
    print(f'{time: } appended to {csv_filename}')

# Set the initial date range
start_date = datetime.strptime('2024-07-09', '%Y-%m-%d')
end_date = start_date + timedelta(days=15)  # Adjust the date range as needed
loop_cnt = 1
while end_date <= datetime.now():
    params = params_base.copy()
    params['q'] += f' created:{start_date.strftime("%Y-%m-%d")}..{end_date.strftime("%Y-%m-%d")}'
    
    page = 1
    while True:
        params['page'] = page

        response = requests.get(url, params=params, headers=headers)
        # print(response.url)
        data = response.json()

        # Check if the request was successful
        if response.status_code == 200:
            repositories = data.get('items', [])

            # Exit the inner loop if no more results
            if not repositories:
                break

            # Extract relevant information
            for repo in repositories:
                full_name = repo.get('full_name', '')
                default_branch = repo.get('default_branch', '')
                forks = repo.get('forks', 0)
                watchers = repo.get('watchers', 0)
                topics = repo.get('topics', [])
                created_at = repo.get('created_at', '')

                all_data.append({
                    'full_name': full_name,
                    'default_branch': default_branch,
                    'forks': forks,
                    'watchers': watchers,
                    'topics': topics,
                    'created_at': created_at,
                })

            # Check if there is another page
            if 'Link' in response.headers and 'rel="next"' in response.headers['Link']:
                page += 1
            else:
                break  # Exit the inner loop if no more pages
        else:
            print(f'Error: {response.status_code}')
            print(data)
            break  # Exit the inner loop if there's an error

    # Move to the next date range
    start_date = end_date + timedelta(days=1)
    end_date = start_date + timedelta(days=15)  # Adjust the date range as needed
    print(f"{end_date} current repos: {len(all_data)}")
    if(loop_cnt % 10 == 0) :
        append_to_csv(end_date)
    loop_cnt += 1
    time.sleep(2)


append_to_csv(end_date)
print(f'Data extracted and saved to {csv_filename}')
