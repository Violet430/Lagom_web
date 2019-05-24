#!/usr/bin/python
# -*- coding: UTF-8 -*-

import sys 
import smtplib
from email.mime.text import MIMEText
from email.header import Header
 
sender = '1798599465@qq.com'
receivers = ['1525625981@qq.com']  # 接收邮件，
content = sys.argv 
# 三个参数：第一个为文本内容，第二个 plain 设置文本格式，第三个 utf-8 设置编码
message = MIMEText(content, 'plain', 'utf-8')
message['From'] = Header("后台管理系统", 'utf-8')   # 发送者
message['To'] =  Header("管理员", 'utf-8')        # 接收者
 
subject = '数据质量报告'
message['Subject'] = Header(subject, 'utf-8')
 
 
try:
    smtpObj = smtplib.SMTP('localhost')
    smtpObj.sendmail(sender, receivers, message.as_string())
    print "邮件发送成功"
except smtplib.SMTPException:
    print "Error: 无法发送邮件"