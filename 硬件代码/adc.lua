
--[[
i2c.setup(0, 4, 3, i2c.SLOW)
i2c.start(0)
i2c.address(0, 0x48, i2c.TRANSMITTER)
i2c.write(0, 0x00)
i2c.stop(0)
]]--
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

 print(humidity())
--测得数据减去40  除以1.4 可得到相对湿度

