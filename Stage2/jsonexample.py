import urllib.request
import json

URL = "http://maps.googleapis.com/maps/api/geocode/json?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA&sensor=false"

googleResponse = urllib.request.urlopen(URL)
print(type(googleResponse))
jsonResponse = json.loads(googleResponse.read())

print(jsonResponse)

test = json.dumps([s['geometry']['location'] for s in jsonResponse['results']], indent=3)
print(test)