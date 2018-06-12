#coding:utf-8
import hashlib
from datetime import datetime
def time_md5(str1):
	time = datetime.now()
	str1 = str1+str(time.year)+str(time.month)+str(time.day)+str(time.hour)+str(time.minute)
	hash1 = hashlib.md5()
	hash1.update(str1)
	return hash1.hexdigest()
	#pass
