#### price-merge-app

json body for request on http://localhost:8090/api/v1/prices/merge

```json
[
    {
        "productCode": "122856",
        "number": 1,
        "depart": 1,
        "begin": "2013.01.20 00:00:00",
        "end": "2013.02.20 23:59:59",
        "value": 11000
    },
    {
        "productCode": "122856",
        "number": 2,
        "depart": 1,
        "begin": "2013.01.15 00:00:00",
        "end": "2013.01.25 23:59:59",
        "value": 92000
    },
    {
        "productCode": "6654",
        "number": 1,
        "depart": 2,
        "begin": "2013.01.12 00:00:00",
        "end": "2013.01.13 00:00:00",
        "value": 4000
    }
]
```
