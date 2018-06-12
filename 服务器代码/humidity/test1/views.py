# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.shortcuts import render

from django.http import HttpResponse

from models import *
from datetime import datetime
import json
from models import User
import time_md5
import random
from stmp import sendmail
from isTokenOverdue import isTokenOverdue
#传值
def index(request):
    temp_mac = request.GET['m']
    temp_humidity = request.GET['h']
    usermac_temp = Usermac.objects.filter(mac = temp_mac)
    if len(usermac_temp) == 0:
        a = "userResult:macNoExist"
    else:
        temp = 100-(int(temp_humidity)*100)/180
        Humidity.objects.create(mac=temp_mac,humidity = temp)  
    a = temp_mac+':'+temp_humidity+':'+str(temp)
    return HttpResponse(a)


#显示
def display(request):
    now_time = datetime.now()
    list_humidity = Humidity.objects.filter(time__year=str(now_time.year),time__month=str(now_time.month),time__day=str(now_time.day))
    list_humidity = list(list_humidity)
    b=''
    for i in list_humidity:
        b = b + 'mac:'+i.mac+'<br>humidity:'+str(i.humidity)+'<br>time:'+str(i.time.hour)+':'+str(i.time.minute)+'<br><br>'
    return HttpResponse(b)



#可视化显示
def chart(request):
    now_time = datetime.now()
    list_humidity = Humidity.objects.filter(time__year=str(now_time.year),time__month=str(now_time.month),time__day=str(now_time.day))
    list_humidity = list(list_humidity)
	#生成字典数据
    list_data = {}
    num = 0
    for i in list_humidity:
        num = num + 1;
        time = str(i.time.hour)+':'+str(i.time.minute)
        list_data[num] = {time:i.humidity}
    return render(request,'home.htm',{'Test': json.dumps(list_data)})

#注册
def sign_up(request):
    user_id = request.GET['uid']
    password = request.GET['pwd']
    code = request.GET['cd']
    temp = User.objects.filter(user_id = user_id)
    code_temp = TempCode.objects.filter(user_id = user_id)
    if len(temp) == 0:
        if len(code_temp) == 0:
            q = "userResult:codeNoExist"
        else:
            if str(list(code_temp)[0].code) == code:
                token = time_md5.time_md5(password)
                User.objects.create(user_id = user_id,password = password,stoken = token)
                q = "userResult:"+token
                code_temp.delete()
            else:
                q = "userResult:codeError"
    else:
        q = "userResult:userIdExist"
    return HttpResponse(q)

#注册验证码获取
def sign_up_code(request):
    user_id = request.GET['uid']
    temp = User.objects.filter(user_id = user_id)
    if len(temp) == 0:
        code = random.randint(100000,999999)
        TempCode.objects.filter(user_id = user_id).delete()
        TempCode.objects.create(user_id = user_id,code = code)
        if sendmail(user_id,code):
            p = "userResult:codeCreate"
        else:
            p = "userResult:codeCreateFail"
    else:
        p = "userResult:userIdExist"
    return HttpResponse(p)

#登陆
def sign_in(request):
    user_id = request.GET['uid']
    password = request.GET['pwd']
    temp = User.objects.filter(user_id = user_id)
    if len(temp) == 0:
        p = "userResult:noUserId"
    else:
        if list(temp)[0].password == password:
            temp.delete()
            token = time_md5.time_md5(password)
            User.objects.create(user_id = user_id,password = password,stoken = token)
            p = "userResult:"+token
        else:
            p = "userResult:wrong"
    return HttpResponse(p)

def compare_token(request):
	user_id = request.GET['uid']
	token = request.GET['tkn']
	temp = User.objects.filter(user_id = user_id)
	if len(temp) == 0:
		p = "userResult:userIdError"
	elif list(temp)[0].stoken == token:
		if isTokenOverdue(list(temp)[0].create_time):
			p = "userResult:outTime"
		else:
			p = "userResult:true"
	else:
		p = "userResult:false"
	return HttpResponse(p)

#湿度数据
def change_password(request):
	user_id = request.GET['uid']
	password = request.GET['pwd']
	new_password = request.GET['npwd']
	token = request.GET['tkn']
	temp = User.objects.filter(user_id = user_id)
	if len(temp) == 0:
		p = "userResult:userIdError"
	elif list(temp)[0].password == password:
		if list(temp)[0].stoken == token:
			if isTokenOverdue(list(temp)[0].create_time):
				p = "userResult:outTime"
			else:
				temp.delete()
				new_token = time_md5.time_md5(new_password)
				User.objects.create(user_id = user_id,password = new_password,stoken = new_token)
				p = "userResult:"+str(new_token)
				#pass
		else:
			p = "userResult:tokenError"
	else:
		p = "userResult:passwordError"
	return HttpResponse(p)

def add_device(request):
	user_id = request.GET['uid']
	token = request.GET['tkn']
	mac = request.GET['mac']
	flower_name = request.GET['fn']
	flower_other_name = request.GET['fon']
	user_temp = User.objects.filter(user_id = user_id)
	if len(user_temp) == 0:
		p = "userResult:userIdError"
	elif list(user_temp)[0].stoken == token:
		if isTokenOverdue(list(user_temp)[0].create_time):
			p = "userResult:outTime"
		else:
			p = add_flower_mac(user_id,mac,flower_name,flower_other_name)
	else:
		p = "userResult:tokenError"
	return HttpResponse(p)

def remove_device(request):
	user_id = request.GET['uid']
	token = request.GET['tkn']
	flower_other_name = request.GET['ofn']
	user_is_legel = is_legel(user_id,token)
	if user_is_legel == "":
		mac = is_other_name_exist(user_id,flower_other_name)
		if mac == "":
			p = "userResult:deviceNoExist"
		else:
			Usermac.objects.filter(mac = mac).delete()
			FlowerMac.objects.filter(mac = mac).delete()
			Humidity.objects.filter(mac = mac).delete()
			p = "userResult:ok"
	else:
		p = user_is_legel
	return HttpResponse(p)

def forgetPassword(request):
	user_id = request.GET['uid']
	list_user = User.objects.filter(user_id = user_id)
	if len(list_user) == 0:
		p = "userResult:userIdNoExist"
	else:
		code = random.randint(100000,999999)
		TempCode.objects.filter(user_id = user_id).delete()
		TempCode.objects.create(user_id = user_id,code = code)
		if sendmail(user_id,code):
			p = "userResult:codeCreate"
		else:
			p = "userResult:codeCreateFail"
	return HttpResponse(p)

def forgetPasswordChange(request):
	user_id = request.GET['uid']
	code = request.GET['cd']
	new_password = request.GET['npwd']
	temp = User.objects.filter(user_id = user_id)
	code_temp = TempCode.objects.filter(user_id = user_id)
	if len(temp) == 0:
		p = "userResult:userIdNoExist"
	else:
		if len(code_temp) == 0:
			p = "userResult:codeNoExist"
		else:
			if str(list(code_temp)[0].code) == code:
				temp.delete()
				new_token = time_md5.time_md5(new_password)
				User.objects.create(user_id = user_id,password = new_password,stoken = new_token)
				p = "userResult:"+str(new_token)
				code_temp.delete()
			else:
				p = "userResult:codeError"
	return HttpResponse(p)

def is_legel(user_id,token):
	user_temp = User.objects.filter(user_id = user_id)
	if len(user_temp) == 0:
		return "userResult:userIdError"
	elif list(user_temp)[0].stoken == token:
		if isTokenOverdue(list(user_temp)[0].create_time):
			return  "userResult:outTime"
		else:
			return ""
	else:
		return "userResult:tokenError"


def add_flower_mac(user_id,mac,flower_name,flower_other_name):
	usermac_temp = Usermac.objects.filter(mac = mac)
	flower_name_temp = FlowerMaxMinHumidity.objects.filter(flower_name = flower_name)
	flower_mac_temp = FlowerMac.objects.filter(mac = mac)
	if len(usermac_temp) == 0:
		if len(flower_name_temp) == 0:
			p = "userResult:flowerNoExist"
		else:
			if len(flower_mac_temp) == 0:
				if is_other_name_exist(user_id,flower_other_name) == "":
					Humidity.objects.filter(mac = mac).delete()
					Usermac.objects.create(user_id = user_id,mac = mac)
					FlowerMac.objects.create(mac = mac,flower_name = flower_name,flower_other_name = flower_other_name)
					Humidity.objects.create(mac = mac,humidity = 0)
					p = "userResult:ok"
				else:
					p = "userResult:flowerOtherNameExist"
			else:
				p = "userResult:flowerMacExist"
	else:
		p = "userResult:deviceExist"
	return p

def is_other_name_exist(user_id,flower_other_name):
	usermac_temp = Usermac.objects.filter(user_id = user_id)
	list_usermac = list(usermac_temp)
	for i in list_usermac:
		flower_mac_temp = FlowerMac.objects.filter(mac = i.mac)
		if list(flower_mac_temp)[0].flower_other_name == flower_other_name:
			return i.mac
	return ""


def getHumidityList(request):
	user_id = request.GET['uid']
	token = request.GET['tkn']
	test = get_mac_list(user_id)	
	return HttpResponse(test)
# Create your views here

def get_mac_list(user_id_temp):
	list_mac = Usermac.objects.filter(user_id=user_id_temp)
	list_mac = list(list_mac)
	temp = ""
	for i in list_mac:
		temp = temp + get_flower_name(i.mac)+";"
	#temp = temp + "占位,占位:0,0,0,0,0,0,0,0"
	#print temp[:-1]
	return temp[:-1]

def get_flower_name(mac_temp):
	flower_name = FlowerMac.objects.filter(mac=mac_temp)
	flower_name = list(flower_name)
	flower_max_min = FlowerMaxMinHumidity.objects.filter(flower_name = flower_name[0].flower_name)
	flower_max_min = list(flower_max_min)
	flower_humidity = Humidity.objects.filter(mac = mac_temp)
	last_humidity = len(flower_humidity)-1
	flower_humidity = list(flower_humidity)
	temp = flower_name[0].flower_name+","+flower_name[0].flower_other_name+":"+str(flower_humidity[last_humidity].humidity)+","+str(flower_max_min[0].max_humidity)+","+str(flower_max_min[0].min_humidity)+","+str(flower_humidity[last_humidity].time.year)+","+str(flower_humidity[last_humidity].time.month)+","+str(flower_humidity[last_humidity].time.day)+","+str(flower_humidity[last_humidity].time.hour)+","+str(flower_humidity[last_humidity].time.minute)
	return temp

