# Student-Academic-Portal

# There are two main functions in this app.
 
# 1. Admin:

1. Admin can add the users and curriculum.
2. It is assumed that there is only a UG curriculum.
3. only users added by the admin only can use the application
4. when a user is added to the table they will automatically get added to the student table or faculty table according to their role.
5. it is assumed that 1 is for the academic office, 2 for a professor, and 3 for the students.

# 2. other for students, academic office, professor

# Student:
1. first should log in with your email and password.
2. they can view grades, compute cgpa, register, or deregister a course.
3. it is assumed that only one course with a course code is available for enrollment in a particular semester.
4. to register for a course
5. for computing cgpa, students should complete that course for credits.

# Professor: 
1. first login.
2. professor can register, deregister himself from the course, and also update the course offering info. he can view the grades of the students who took that professor's course.
3. it is assumed that he can only offer one course in one semester with the same course code.
4. if he deregisters a course, student registration info who are enrolled in that course will be deleted.

# academic office:
1. first login.
2. they can edit course catalogs, view the grades of all the students, and generate transcripts of particular students.
3. unless that user logout, he can do any of his respective works.
4. if we do not want to close, press 1 and log in as another user.

the database and uml and unit test planning files are also attached.
