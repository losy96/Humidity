print('setup_i2c')
i2c.setup(0, 4, 3, i2c.SLOW)--sda  scl
i2c.start(0)
i2c.address(0, 0x48, i2c.TRANSMITTER)
i2c.write(0, 0x00)
i2c.stop(0)
