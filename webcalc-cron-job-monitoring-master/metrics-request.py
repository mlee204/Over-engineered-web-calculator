import requests
import yaml

data = yaml.safe_load(open('config.yaml'))
endpoint = data['url']

requests.post(endpoint)
