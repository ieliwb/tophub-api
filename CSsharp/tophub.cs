using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Threading.Tasks;

class Tophub
{
	private readonly string baseurl = "https://api.tophubdata.com";
	private readonly string apikey;

	public Tophub(string apikey = "")
	{
		this.apikey = apikey;
	}

	public async Task<Dictionary<string, object>> Nodes(int p = 1)
	{
		return await Call("/nodes", new Dictionary<string, object> { { "p", p } });
	}

	public async Task<Dictionary<string, object>> Node(string hashid)
	{
		return await Call($"/nodes/{hashid}", new Dictionary<string, object>());
	}

	public async Task<Dictionary<string, object>> NodeHistorys(string hashid, string date)
	{
		return await Call($"/nodes/{hashid}/historys", new Dictionary<string, object> { { "date", date } });
	}

	public async Task<Dictionary<string, object>> Search(string q, int p = 1, string hashid = "")
	{
		return await Call("/search", new Dictionary<string, object> { { "q", q }, { "p", p }, { "hashid", hashid } });
	}

	private async Task<Dictionary<string, object>> Call(string endpoint, Dictionary<string, object> parameters)
	{
		string url = $"{baseurl}{endpoint}";
		if (parameters.Count > 0)
		{
			url += "?" + string.Join("&", parameters.Select(p => $"{Uri.EscapeDataString(p.Key)}={Uri.EscapeDataString(p.Value.ToString())}"));
		}

		using (HttpClient client = new HttpClient())
		{
			client.DefaultRequestHeaders.Add("Authorization", apikey);

			HttpResponseMessage response = await client.GetAsync(url);
			if (response.IsSuccessStatusCode)
			{
				string json = await response.Content.ReadAsStringAsync();
				return Newtonsoft.Json.JsonConvert.DeserializeObject<Dictionary<string, object>>(json);
			}
			else
			{
				Console.WriteLine($"Request failed with status code {response.StatusCode}");
				return null;
			}
		}
	}
}

class Program
{
	static async Task Main()
	{
		// Example usage
		string apikey = "";
		Tophub tophub = new Tophub(apikey);
		Dictionary<string, object> resultNodes = await tophub.Nodes(1);
		Dictionary<string, object> resultNode = await tophub.Node("mproPpoq6O");
		Dictionary<string, object> resultNodeHistorys = await tophub.NodeHistorys("mproPpoq6O", "2023-01-01");
		Dictionary<string, object> resultSearch = await tophub.Search("苹果", 1, "mproPpoq6O");
	}
}
