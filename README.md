# About database archival service

This is a springboot based service that periodically checks for data that needs to be archived from the service database and move it to an archive database.

## Working features:

- Admin creates the archival policies through (/archive/rules) api. As part of api admin provides service database information and admin also provides how old data should be inorder to archive. Admin can also provide duration till which the archived data should be retained.
- A scheduled cron job gets triggered (at off-peak hours) when the old data from service database is moved to the archive database.
- Another scheduled cron job also gets triggered inorder to delete older data from archive database.
- Owner of the data has permission to read the data from archived database.
- Admin has access to read all the data from the archived database.

### Design document
The design document and more project information is added at:
link: https://docs.google.com/document/d/1XaUoWFo__smLnKvKQfBP-MQhLou6XjRAzHALD1tHY7g/edit?usp=sharing