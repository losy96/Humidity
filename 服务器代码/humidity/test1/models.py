# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models
import datetime
class Humidity(models.Model):
    mac = models.CharField(max_length=30)
    humidity = models.IntegerField()
    time = models.DateTimeField(default=datetime.datetime.now)
    def __unicode__(self):
        return self.mac

class User(models.Model):
    user_id = models.CharField(max_length=30)
    password = models.CharField(max_length=40)
    stoken = models.CharField(max_length=40)
    create_time = models.DateTimeField(default=datetime.datetime.now)

class Usermac(models.Model):
    user_id = models.CharField(max_length=30)
    mac = models.CharField(max_length=30)

class FlowerMac(models.Model):
	mac = models.CharField(max_length = 30,null=True)
	flower_name = models.CharField(max_length=30)
	flower_other_name = models.CharField(max_length=30)

class FlowerMaxMinHumidity(models.Model):
	flower_name = models.CharField(max_length=30)
	max_humidity = models.IntegerField()
	min_humidity = models.IntegerField()

class TempCode(models.Model):
	user_id = models.CharField(max_length=30)
	code = models.IntegerField()
# Create your models here.
