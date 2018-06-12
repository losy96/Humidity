require("http")
http.get("http://120.79.52.102/?m=node&h=123", nil, function(code, data)
    if (code < 0) then
      print("HTTP request failed")
    else
      print(code, data)
    end
  end)
