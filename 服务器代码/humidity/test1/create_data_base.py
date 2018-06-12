coding:utf-8
from models import Humidity
from models import FlowerMac
from models import User
from models import Usermac
from models import FlowerMaxMinHumidity

def create_user_mac():
	Usermac.objects.create(user_id = "843962249@qq.com",mac = "00-01-6C-06-A6-29")
	Usermac.objects.create(user_id = "843962249@qq.com",mac = "00-01-6C-06-A6-24")
	Usermac.objects.create(user_id = "843962249@qq.com",mac = "00-01-6C-06-A6-23")
	Usermac.objects.create(user_id = "843962249@qq.com",mac = "00-01-6C-06-A6-26")
	Usermac.objects.create(user_id = "843962249@qq.com",mac = "00-01-6C-06-A6-27")
	Usermac.objects.create(user_id = "843962249@qq.com",mac = "00-01-6C-06-A6-28")

