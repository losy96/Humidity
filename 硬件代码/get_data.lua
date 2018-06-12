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
print("now_humidity:")
print(humidity())
