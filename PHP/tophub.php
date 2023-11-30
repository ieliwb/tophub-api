<?php

class Tophub {
	
	protected $baseurl = 'https://api.tophubdata.com';
	protected $apikey = '';
	
	public function __construct($apikey = '') {
		$this->apikey = $apikey;
	}
	
	public function nodes($p = 1) {
		return $this->call("/nodes", ["p"=>$p]);
	}
	
	public function node($hashid) {
		return $this->call("/nodes/{$hashid}", []);
	}
	
	public function nodeHistorys($hashid, $date) {
		return $this->call("/nodes/{$hashid}/historys", ["date"=>$date]);
	}
	
	public function search($q, $p = 1, $hashid='') {
		return $this->call("/search", ["q"=>$q, "p"=>$p, "hashid"=>$hashid]);
	}
	

	public function call($endpoint, $params = []) {
		$url = $this->baseurl . $endpoint;
		if(!empty($params)){
			$url .= ("?" . http_build_query($params));
		}
		
		$curl = curl_init();
		curl_setopt_array($curl, array(
			CURLOPT_URL => $url,
			CURLOPT_RETURNTRANSFER => true,
			CURLOPT_MAXREDIRS => 10,
			CURLOPT_TIMEOUT => 30,
			CURLOPT_CUSTOMREQUEST => 'GET',
			CURLOPT_HTTPHEADER => array(
				'Authorization: ' . $this->apikey 
			),
		));
		$response = curl_exec($curl);
		curl_close($curl);
		return json_decode($response, 1);
	}
}

//演示
$apikey = '';
$tophub = new Tophub($apikey);
$result_nodes = $tophub->nodes(1);
$result_node = $tophub->node('mproPpoq6O');
$result_node_historys = $tophub->nodeHistorys('mproPpoq6O', '2023-01-01');
$result_search = $tophub->search('苹果', 1, 'mproPpoq6O');

