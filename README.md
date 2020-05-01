# Vetal
Build status: [![build_status](https://travis-ci.com/AnGo84/Vetal.svg?branch=master)](https://travis-ci.com/AnGo84/Vetal.svg)
[![BCH compliance](https://bettercodehub.com/edge/badge/AnGo84/Vetal?branch=master)](https://bettercodehub.com/)
- - -
CRUD Web application for managing applications in the printing house.
Provides separation of access to data by roles ***Administrator, Manager, Accountant***.
Realised two types of applications: ***task*** and ***stencil***.
After add or edit, **Tasks** can be send to partner by email.
For printing applications used export to PDF format.
Also, user can filtered data and export result to Excel format.

**To using necessary:**

- IDE
- JDK 1.8 or later
- Maven
- MySQL database
- Jasper report

### Realise 1.5 INFO
In progress
Add:
- test coverage;

### Realise 1.0 INFO

Stable version with realisation:
- tasks: add, edit, delete, send to contractor, reports;
- stencils: add, edit, delete, reports, calculate 'ink-prints' by days;
- users: add, edit, delete, access by roles, password managing;
- directories: add, edit, delete;

### TODO
- Refactoring code;
- Change view filters logic;