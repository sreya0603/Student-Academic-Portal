1.There are two main functions in this app.
->one for the admin:

Admin can add the users and curriculum.
It is assumed that there is only UG curriculum.
only users added by the admin only can use the application
when a user is added to the table they will automatically get added to the student table or faculty table according to their role.
it is assumed that 1 is for academic offic, 2 for professor, 3 for students.

->other for students,academicoffice,professor

student: first should login with email and password.
they can view grades,compute cgpa, register of deregister a course.
it is assumed that only one course with a course code is available for enrollment in particcular semester.
to register a course 
for computing cgpa , student should complete that course for credits.

professor: first login.
professor can register,deregister himself from the course,and also update the course offering info.he can view grades of the students who took that
professor's course.
it is assumed that he can only offer one course in one semester with same course code.
if he deregister a course, student register info who are enrolled in that course will be deleted.

academic office: first login.
they can edit course catalog, view grades of all the students,generate transcript of particular student.

unless that user logout, he can do any of his respective works.
if we donot want to close, press 1 and login as another user.

the database and uml and unit test planning files are also attached.
