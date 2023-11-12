require 'net/http'
require 'json'

class Tophub
  def initialize(apikey = '')
	@baseurl = 'https://api.tophubdata.com'
	@apikey = apikey
  end

  def nodes(p = 1)
	call('/nodes', { 'p' => p })
  end

  def node(hashid)
	call("/nodes/#{hashid}", {})
  end

  def node_historys(hashid, date)
	call("/nodes/#{hashid}/historys", { 'date' => date })
  end

  def search(q, p = 1, hashid = '')
	call('/search', { 'q' => q, 'p' => p, 'hashid' => hashid })
  end

  def call(endpoint, params = {})
	url = "#{@baseurl}#{endpoint}"
	url += "?#{URI.encode_www_form(params)}" unless params.empty?

	uri = URI(url)
	request = Net::HTTP::Get.new(uri)
	request['Authorization'] = @apikey

	response = Net::HTTP.start(uri.hostname, uri.port, use_ssl: uri.scheme == 'https') do |http|
	  http.request(request)
	end

	if response.is_a?(Net::HTTPSuccess)
	  JSON.parse(response.body)
	else
	  puts "Request failed with status code #{response.code}"
	  nil
	end
  end
end

# Example usage
apikey = ''
tophub = Tophub.new(apikey)
result_nodes = tophub.nodes(1)
result_node = tophub.node('mproPpoq6O')
result_node_historys = tophub.node_historys('mproPpoq6O', '2023-01-01')
result_search = tophub.search('苹果', 1, 'mproPpoq6O')

puts result_nodes
puts result_node
puts result_node_historys
puts result_search
