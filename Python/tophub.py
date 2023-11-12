import requests

class Tophub:

	def __init__(self, apikey=''):
		self.baseurl = 'https://api.tophubdata.com'
		self.apikey = apikey

	def nodes(self, p=1):
		return self.call("/nodes", {"p": p})

	def node(self, hashid):
		return self.call(f"/nodes/{hashid}", {})

	def node_historys(self, hashid, date):
		return self.call(f"/nodes/{hashid}/historys", {"date": date})

	def search(self, q, p=1, hashid=''):
		return self.call("/search", {"q": q, "p": p, "hashid": hashid})

	def call(self, endpoint, params={}):
		url = f"{self.baseurl}{endpoint}"
		if params:
			url += "?" + "&".join([f"{key}={value}" for key, value in params.items()])

		headers = {'Authorization': self.apikey}
		response = requests.get(url, headers=headers)

		if response.status_code == 200:
			return response.json()
		else:
			print(f"Request failed with status code {response.status_code}")
			return None

# Example usage
apikey = ''
tophub = Tophub(apikey)
result_nodes = tophub.nodes(1)
result_node = tophub.node('mproPpoq6O')
result_node_historys = tophub.node_historys('mproPpoq6O', '2023-01-01')
result_search = tophub.search('苹果', 1, 'mproPpoq6O')
