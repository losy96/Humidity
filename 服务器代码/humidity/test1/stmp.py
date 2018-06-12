# -*- coding: UTF-8 -*-
 
import smtplib
from email.mime.text import MIMEText
from email.header import Header
import random
def sendmail(receivers,code):
	# 第三方 SMTP 服务
	mail_host="smtp.163.com"  #设置服务器
	mail_user="lihaoKindle@163.com"    #用户名
	mail_pass="humidity1234"   #口令 
	sender = 'lihaoKindle@163.com'
	#receivers = '843962249@qq.com'  # 接收邮件，可设置为你的QQ邮箱或者其他邮箱
	message = MIMEText('您修改密码所需的验证码为：'+str(code), 'plain', 'utf-8')
	message['From'] = mail_user
	message['To'] =  receivers
	message['Cc'] = mail_user
	subject = '湿度水平监测'
	message['Subject'] = Header(subject, 'utf-8') 
	try:
		smtpObj = smtplib.SMTP_SSL(mail_host,465) 
		#smtpObj.SMTP_SSL(mail_host, 465)    # 25 为 SMTP 端口号
		smtpObj.login(mail_user,mail_pass)
		smtpObj.sendmail(sender,[receivers,mail_user], message.as_string())
		print "邮件发送成功"
		return 1
	except smtplib.SMTPException,e:
		print e
		print "Error: 无法发送邮件"
		return 0
if __name__ == "__main__":
	sendmail('843962249@qq.com',random.randint(100000,999999))
