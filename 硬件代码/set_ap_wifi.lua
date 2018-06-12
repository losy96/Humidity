wifi.setmode(wifi.STATIONAP)
wifi.ap.config({ssid="humidity1", pwd="12345678", auth=wifi.WPA2_PSK})
