#coding:utf-8
import datetime
def isTokenOverdue(time):
	#返回是否过期 true为过期
	now_time = datetime.datetime.now() - datetime.timedelta(minutes = 30)
	return now_time > time

