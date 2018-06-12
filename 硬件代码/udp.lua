mac = wifi.sta.getmac()
--get mac addres
i2c.start(0)
i2c.address(0, 0x48, i2c.RECEIVER)
c = i2c.read(0, 1)
i2c.stop(0)

cl = net.createConnection(net.UDP, 0)
cl:connect(1234, "10.63.45.22")
cl:send(mac.."\n"..string.byte(c))