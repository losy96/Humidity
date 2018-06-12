sv = net.createServer(net.TCP, 30)
--status = wifi.sta.status()
function receiver(sck, data)
    if data=="1" then
        print("try status")
        sck:close()
    else
        ssid=string.match(data,"[^\n]+")
        password=string.match(data,"[^\n]+$")
        if password == nil then
            print("ii")
            password = "nil"
        else
            wifi.sta.config(ssid,password)
        end
        print("ssid:"..ssid)
        print("password:"..password)
        sck:close()
    end
end

if sv then
  sv:listen(80, function(conn)
    conn:on("receive", receiver)
    status =  wifi.sta.status()..":"..wifi.sta.getmac()
    conn:send(status)
  end)
end
