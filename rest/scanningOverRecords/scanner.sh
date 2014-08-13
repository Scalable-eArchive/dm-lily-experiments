curl -XPOST localhost:12060/repository/scan -H 'Content-Type: application/json' -d '
{
recordFilter: {
"@class": "org.lilyproject.repository.api.filter.RecordTypeFilter",
recordType: "{my.demo}product"
},
caching: 10
}' -D -
