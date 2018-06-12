function humidity()
    i2c.start(0)
    --1 read 0 write
    i2c.address(0, 0x48, i2c.RECEIVER)
    c = i2c.read(0, 1)
    i2c.stop(0)
    --print(string.byte(c))
    h = string.byte(c)
    --print(h)
    return h
end
hum = humidity()
print(hum)
mac = wifi.sta.getmac()
print(mac)

--up_data
conn=net.createConnection(net.TCP, false) 
conn:on("receive", function(conn, pl) print(p1) end)
conn:connect(80,"120.79.52.102")
conn:send("GET /add/?m="..mac.."&h="..hum.." HTTP/1.1\r\nHost:120.79.52.102\r\n\r\n") 

