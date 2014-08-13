curl -XPOST localhost:12060/repository/record -H 'Content-Type: application/json' -d '
{
action : "create",
record: {
type: "n$file",
fields: {
n$data: {
"value": "D6PX49PARGq_ljTyJaLYjkhERlMAAAAE",
"mediaType": "application/pdf",
"size": 1228570
}
},
namespaces: { "my.demo": "n" }
}
}' -D -

