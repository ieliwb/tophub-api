package main

import (
	"encoding/json"
	"fmt"
	"io/ioutil"
	"net/http"
	"net/url"
)

// Tophub represents the Tophub API client
type Tophub struct {
	baseURL string
	apikey  string
}

// NewTophub creates a new instance of the Tophub API client
func NewTophub(apikey string) *Tophub {
	return &Tophub{
		baseURL: "https://api.tophubdata.com",
		apikey:  apikey,
	}
}

// Nodes retrieves nodes from the Tophub API
func (t *Tophub) Nodes(p int) (map[string]interface{}, error) {
	return t.call("/nodes", map[string]interface{}{"p": p})
}

// Node retrieves a specific node from the Tophub API
func (t *Tophub) Node(hashid string) (map[string]interface{}, error) {
	return t.call("/nodes/"+hashid, map[string]interface{}{})
}

// NodeHistorys retrieves node history from the Tophub API
func (t *Tophub) NodeHistorys(hashid, date string) (map[string]interface{}, error) {
	return t.call("/nodes/"+hashid+"/historys", map[string]interface{}{"date": date})
}

// Search performs a search on the Tophub API
func (t *Tophub) Search(q string, p int, hashid string) (map[string]interface{}, error) {
	return t.call("/search", map[string]interface{}{"q": q, "p": p, "hashid": hashid})
}

// call is a generic function to make API calls
func (t *Tophub) call(endpoint string, params map[string]interface{}) (map[string]interface{}, error) {
	url := t.baseURL + endpoint
	if len(params) > 0 {
		queryString := url.Values{}
		for key, value := range params {
			queryString.Add(key, fmt.Sprintf("%v", value))
		}
		url += "?" + queryString.Encode()
	}

	req, err := http.NewRequest("GET", url, nil)
	if err != nil {
		return nil, err
	}

	req.Header.Set("Authorization", t.apikey)

	resp, err := http.DefaultClient.Do(req)
	if err != nil {
		return nil, err
	}
	defer resp.Body.Close()

	body, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		return nil, err
	}

	var result map[string]interface{}
	err = json.Unmarshal(body, &result)
	if err != nil {
		return nil, err
	}

	return result, nil
}

func main() {
	// Example usage
	apikey := ""
	tophub := NewTophub(apikey)
	resultNodes, _ := tophub.Nodes(1)
	resultNode, _ := tophub.Node("mproPpoq6O")
	resultNodeHistorys, _ := tophub.NodeHistorys("mproPpoq6O", "2023-01-01")
	resultSearch, _ := tophub.Search("苹果", 1, "mproPpoq6O")

	fmt.Println(resultNodes)
	fmt.Println(resultNode)
	fmt.Println(resultNodeHistorys)
	fmt.Println(resultSearch)
}
