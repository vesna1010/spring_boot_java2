delete from users;
delete from departments;
delete from study_programs;
delete from subjects_professors;
delete from subjects;
delete from professors;
delete from students;
delete from exams;

insert into users(id, name, email, password, authority, enabled)
values(1, 'User A', 'userA@gmail.com', '$2a$10$In56r7xIvRtHPVm/HJq/Mun0aFlhKdwFlYmU5TfKChUVlJ7VimW2O', 'ADMIN', true);

insert into users(id, name, email, password, authority, enabled)
values(2, 'User B', 'userB@gmail.com', '$2a$10$G7jWAKmlsyxZhpFQewOVmOnmPcA/GRDhWuJHX6J4.S/AiOZDJMF.i', 'USER', true);

insert into users(id, name, email, password, authority, enabled)
values(3, 'User C', 'userC@gmail.com', '$2a$10$Wh9LdX9cePtAwx0jornObOzsK7K5.dsneeJfsc7..MlRgVfLTMAnu', 'PROFESSOR', true);

insert into departments(id, name, created_on) values(1, 'Department B', '2018-09-01');

insert into departments(id, name, created_on) values(2, 'Department A', '2018-09-02');

insert into study_programs(id, name, created_on, duration, department_id) 
values(1, 'Study Program B', '2018-09-01', 3, 1);

insert into study_programs(id, name, created_on, duration, department_id) 
values(2, 'Study Program C', '2018-09-02', 3, 2);

insert into study_programs(id, name, created_on, duration, department_id) 
values(3, 'Study Program A', '2018-09-01', 3, 1);

insert into subjects(id, name, study_program_id) values(1, 'Subject B', 1);

insert into subjects(id, name, study_program_id) values(2, 'Subject D', 2);

insert into subjects(id, name, study_program_id) values(3, 'Subject A', 1);

insert into subjects(id, name, study_program_id) values(4, 'Subject C', 3);

insert into professors(id, name, parent, birth_date, email, telephone, gender, address, photo, title)
value(1, 'Professor B', 'Parent B', '1972-12-11', 'professorB@gmail.com', '065 123 123', 'MALE', 'Address B', 
LOAD_FILE('C:\\ProgramData\\MySQL\\MySQL Server 5.7\\Uploads\\image.jpg'), 'Title B');

insert into professors(id, name, parent, birth_date, email, telephone, gender, address, photo, title)
value(2, 'Professor C', 'Parent C', '1975-05-23', 'professorC@gmail.com', '065 123 124', 'MALE', 'Address C', 
LOAD_FILE('C:\\ProgramData\\MySQL\\MySQL Server 5.7\\Uploads\\image.jpg'), 'Title C');

insert into professors(id, name, parent, birth_date, email, telephone, gender, address, photo, title)
value(3, 'Professor A', 'Parent A', '1978-08-29', 'professorA@gmail.com', '065 123 125', 'MALE', 'Address A', 
LOAD_FILE('C:\\ProgramData\\MySQL\\MySQL Server 5.7\\Uploads\\image.jpg'), 'Title A');

insert into subjects_professors(subject_id, professor_id) values(1, 1);
insert into subjects_professors(subject_id, professor_id) values(1, 2);
insert into subjects_professors(subject_id, professor_id) values(3, 1);

insert into subjects_professors(subject_id, professor_id) values(2, 2);
insert into subjects_professors(subject_id, professor_id) values(2, 3);
insert into subjects_professors(subject_id, professor_id) values(4, 3);

insert into students(id, name, parent, birth_date, email, telephone, gender, 
address, photo, start_date, end_date, year, study_program_id)
value(1, 'Student C', 'Parent C', '1995-12-22', 'studentC@gmail.com', '065 312 222', 'MALE', 'Address C', 
LOAD_FILE('C:\\ProgramData\\MySQL\\MySQL Server 5.7\\Uploads\\image.jpg'), '2019-01-01', null, 1, 1);

insert into students(id, name, parent, birth_date, email, telephone, gender, 
address, photo, start_date, end_date, year, study_program_id)
value(2, 'Student B', 'Parent B', '1995-10-02', 'studentB@gmail.com', '065 314 252', 'MALE', 'Address B', 
LOAD_FILE('C:\\ProgramData\\MySQL\\MySQL Server 5.7\\Uploads\\image.jpg'), '2019-01-01', null, 1, 1);

insert into students(id, name, parent, birth_date, email, telephone, gender, 
address, photo, start_date, end_date, year, study_program_id)
value(3, 'Student A', 'Parent A', '1995-09-11', 'studentA@gmail.com', '065 812 220', 'MALE', 'Address A', 
LOAD_FILE('C:\\ProgramData\\MySQL\\MySQL Server 5.7\\Uploads\\image.jpg'), '2019-01-01', null, 1, 2);

insert into exams(date, student_id, professor_id, subject_id, score)
values('2019-02-10', 1, 1, 1, 8);

insert into exams(date, student_id, professor_id, subject_id, score)
values('2019-02-13', 1, 1, 3, 9);

insert into exams(date, student_id, professor_id, subject_id, score)
values('2019-02-10', 2, 2, 1, 7);

insert into exams(date, student_id, professor_id, subject_id, score)
values('2019-02-10', 3, 2, 2, 7);












