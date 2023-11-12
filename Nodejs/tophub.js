const axios = require('axios');

class Tophub {
	constructor(apikey = '') {
		this.baseurl = 'https://api.tophubdata.com';
		this.apikey = apikey;
	}

	async nodes(p = 1) {
		return this.call('/nodes', { p });
	}

	async node(hashid) {
		return this.call(`/nodes/${hashid}`, {});
	}

	async nodeHistorys(hashid, date) {
		return this.call(`/nodes/${hashid}/historys`, { date });
	}

	async search(q, p = 1, hashid = '') {
		return this.call('/search', { q, p, hashid });
	}

	async call(endpoint, params = {}) {
		const url = `${this.baseurl}${endpoint}`;
		const headers = { 'Authorization': this.apikey };

		try {
			const response = await axios.get(url, { params, headers });
			return response.data;
		} catch (error) {
			console.error(`Request failed with status code ${error.response.status}`);
			return null;
		}
	}
}

// Example usage
const apikey = '';
const tophub = new Tophub(apikey);

(async () => {
	const resultNodes = await tophub.nodes(1);
	const resultNode = await tophub.node('mproPpoq6O');
	const resultNodeHistorys = await tophub.nodeHistorys('mproPpoq6O', '2023-01-01');
	const resultSearch = await tophub.search('苹果', 1, 'mproPpoq6O');
})();
