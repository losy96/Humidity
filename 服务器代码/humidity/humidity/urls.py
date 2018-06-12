"""humidity URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/1.11/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  url(r'^$', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  url(r'^$', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.conf.urls import url, include
    2. Add a URL to urlpatterns:  url(r'^blog/', include('blog.urls'))
"""
from django.conf.urls import url
from django.contrib import admin
from test1 import views as test_views

urlpatterns = [
    url(r'^add',test_views.index),
    url(r'^display/$',test_views.display),
    #url(r'^home/$',test_views.home),
    url(r'^chart/$',test_views.chart),
    #url(r'^static/(?P<path>.*)$','django.views.static.serve',{'document_root':settings.STATIC_ROOT},name='static'),
    url(r'^sign_up/',test_views.sign_up),
    url(r'^sign_in',test_views.sign_in),
    url(r'^compare_token',test_views.compare_token),
    url(r'^change_password',test_views.change_password),
    url(r'^getData/',test_views.getHumidityList),
    url(r'^device_add/',test_views.add_device),
    url(r'^device_remove/',test_views.remove_device),
    url(r'^forget_password_code/',test_views.forgetPassword),
    url(r'^forget_password_change/',test_views.forgetPasswordChange),
    url(r'^sign_up_code/',test_views.sign_up_code),
    url(r'^admin/', admin.site.urls)
]
