#!/usr/bin/env python2

import urllib, urllib2, cookielib, getpass

# Set up cookie collector, urllib
cookie_jar = cookielib.CookieJar()
opener = urllib2.build_opener(urllib2.HTTPCookieProcessor(cookie_jar))
urllib2.install_opener(opener)

url1 = "https://login.oregonstate.edu/cas/login?service=https%3A%2F%2Fuhds.oregonstate.edu%2Fmyuhds%2F"

req = urllib2.Request(url1)
rsp = urllib2.urlopen(req)

response = rsp.read().splitlines()

# in app, these values shouldn't be hard-coded but found from the page algorithmically
lt_value = response[98][48:87]
execution_value = response[99][55:59]

print lt_value, execution_value

username = raw_input("Please enter your ONID username: ")
password = getpass.getpass("Please enter your password: ")

# values required for doing HTTP POST
values = dict(username=username, password=password, lt=lt_value, execution='e1s1', _eventId='submit', submit='LOGIN')

data = urllib.urlencode(values)

url2 = "https://login.oregonstate.edu/cas/login?service=https%3A%2F%2Fuhds.oregonstate.edu%2Fmyuhds%2F"

req = urllib2.Request(url2, data)
rsp = urllib2.urlopen(req)
content = rsp.read().splitlines()

print("\nMealplan remaining: ")

print content

for line in content:
	if('$' in line):
		print(line[8:32])
