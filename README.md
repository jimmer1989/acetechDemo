# acetechDemo

# this is a springboot project that can be built in maven.
# i have been doing this in eclipse 

#use postman to PUT your message to URL 
#http://localhost:8080/



#here some suggestions

#<!--this is a successful/correct message***************************************************************************************************************--> 

[{
	"batchid": "PP25604",
	"batchTypeId": "medicine 01",
	"batchTypeDescription": "Panadol",
	"batchExpirationDate": "2024-02-22",
	"batchCount": 101
}, {
	"batchid": "PP25605",
	"batchTypeId": "medicine 03",
	"batchTypeDescription": "Nurofen",
	"batchExpirationDate": "2024-01-22",
	"batchCount": 100
}]

#<!--Fail, not an array ************************************************************************************************************************-->

{
	"batchid": "PP25605",
	"batchTypeId": "medicine 03",
	"batchTypeDescription": "Nurofen",
	"batchExpirationDate": "2024-01-22",
	"batchCount": 100
}

#<!--Fail, batch ID missing from one ************************************************************************************************************************-->
[{
	"batchid": "",
	"batchTypeId": "medicine 01",
	"batchTypeDescription": "Panadol",
	"batchExpirationDate": "2024-02-22",
	"batchCount": 55
}, {
	"batchid": "PP25604",
	"batchTypeId": "medicine 01",
	"batchTypeDescription": "Panadol",
	"batchExpirationDate": "2024-02-22",
	"batchCount": 55
}, {
	"batchid": "PP25605",
	"batchTypeId": "medicine 03",
	"batchTypeDescription": "Nurofen",
	"batchExpirationDate": "2024-02-22",
	"batchCount": 100
}]
#<!--Fail, duplicates ************************************************************************************************************************-->
[{
	"batchid": "PP25604",
	"batchTypeId": "medicine 01",
	"batchTypeDescription": "Panadol",
	"batchExpirationDate": "2024-02-22",
	"batchCount": 55
}, {
	"batchid": "PP25604",
	"batchTypeId": "medicine 01",
	"batchTypeDescription": "Panadol",
	"batchExpirationDate": "2024-02-22",
	"batchCount": 55
}, {
	"batchid": "PP25605",
	"batchTypeId": "medicine 03",
	"batchTypeDescription": "Nurofen",
	"batchExpirationDate": "2024-02-22",
	"batchCount": 100
}]
#<!--Fail, empty array************************************************************************************************************************-->
[]

#<!-- Fail, not an array************************************************************************************************************************-->

{}

#<!--Fail, not json************************************************************************************************************************-->

fxgjzsrtazrtjzratj

#<!--Fail,one value missing from each ******************************************************************************************************-->

[
	{
		"batchid": "PP25604",
		"batchTypeDescription": "Panadol",
		"batchExpirationDate": "2024-02-22",
		"batchCount": "cat"
	}, {
		"batchid": "PP25605",
		"batchTypeId": "medicine 03",
		"batchExpirationDate": "BADDATE",
		"batchCount": 100
	}, {
		"batchid": "PP25605",
		"batchTypeId": "medicine 03",
		"batchTypeDescription": "Nurofen",
		"batchCount": 100
	}, {
		"batchid": "PP25605",
		"batchTypeId": "medicine 03",
		"batchTypeDescription": "Nurofen",
		"batchExpirationDate": "BADDATE"
	}
]

