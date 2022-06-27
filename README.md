Spring Boot CRUD application with React integration. Topic of the project - gift certificates. 

The system exposes REST APIs endpoints to perform the following operations:
1)CRUD operations for GiftCertificate. If new tags are passed during creation/modification – they are created in the DB.
For update operation - updated only fields, that are passed in request, others are not updated. 
2)CRD operations for Tag.
3)Get certificates with tags:
-by tag name (ONE tag)
-search by part of name/description
-sort by date or by name ASC/DESC  (implemented ability to apply both sort type at the same time)
4)CRUD for users
5)CRUD for orders

Pagination and sorting is used for all GET endpoints.
Spring Data Jpa is integrated in app for database content access.
Securing Web application is implemented through Spring Security (JWT and OAuth2 is used).
Process of continious delivery and deployment (CI-CD) is organized using Jenkins.
Application have been deployed on AWS.
Frontend written on ReactJS.
HATEOAS is used for user to have ability to navigate response content.
Stream API is used in application. 
Build Tool - Maven. 
Testing library - JUnit and Mockito.
