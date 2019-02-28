CREATE DATABASE database;
 
USE databae;
    
CREATE TABLE monitor (
id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
ip VARCHAR(30) NOT NULL,
cpu VARCHAR(30) NOT NULL,
tpms VARCHAR(50),
fpms VARCHAR(50),
reg_date TIMESTAMP
); 
    
CREATE TABLE ipstage (
id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
ip VARCHAR(30) NOT NULL,
stage int(6) NOT NULL,
reg_date TIMESTAMP
);
    
//ips from first VMs
//insert into ipstage (ip,stage) values ('192.168.80.51', 1);
    
CREATE TABLE stagecontroller (
id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
ip VARCHAR(30) NOT NULL,
stage int(6) NOT NULL,
reg_date TIMESTAMP
);

  
CREATE TABLE atual (
id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
task VARCHAR(6) NOT NULL,
stage INT(6) NOT NULL,
reg_date TIMESTAMP
);
  

insert into atual(task,stage) value ('x',1);
insert into atual(task,stage) value ('x',2);
insert into atual(task,stage) value ('x',3);
    

// x when it starts
// task number while it processes
// y when it finishes

    
CREATE TABLE general (
id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
ntasks int(6) NOT NULL,
starttime  bigint NOT NULL,
reg_date TIMESTAMP
);
 
//tasks number   
insert into general (ntasks) values (40,0);    


CREATE TABLE taskQueue (
id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
task VARCHAR(20) NOT NULL,
stage VARCHAR(2) NOT NULL,    
reg_date TIMESTAMP
);    
  
    
//insert all tasks in stage (1 in this case)
insert into taskQueue (task,stage) value ('image1.jpg',1);
insert into taskQueue (task,stage) value ('image2.jpg',1);
insert into taskQueue (task,stage) value ('image3.jpg',1);
insert into taskQueue (task,stage) value ('image4.jpg',1);
insert into taskQueue (task,stage) value ('image5.jpg',1);
insert into taskQueue (task,stage) value ('image6.jpg',1);
insert into taskQueue (task,stage) value ('image7.jpg',1);
insert into taskQueue (task,stage) value ('image8.jpg',1);
insert into taskQueue (task,stage) value ('image9.jpg',1);
insert into taskQueue (task,stage) value ('image10.jpg',1);
insert into taskQueue (task,stage) value ('image11.jpg',1);
insert into taskQueue (task,stage) value ('image12.jpg',1);
insert into taskQueue (task,stage) value ('image13.jpg',1);
insert into taskQueue (task,stage) value ('image14.jpg',1);
insert into taskQueue (task,stage) value ('image15.jpg',1);
insert into taskQueue (task,stage) value ('image16.jpg',1);
insert into taskQueue (task,stage) value ('image17.jpg',1);
insert into taskQueue (task,stage) value ('image18.jpg',1);
insert into taskQueue (task,stage) value ('image19.jpg',1);
insert into taskQueue (task,stage) value ('image20.jpg',1);
insert into taskQueue (task,stage) value ('image21.jpg',1);
insert into taskQueue (task,stage) value ('image22.jpg',1);
insert into taskQueue (task,stage) value ('image23.jpg',1);
insert into taskQueue (task,stage) value ('image24.jpg',1);
insert into taskQueue (task,stage) value ('image25.jpg',1);
insert into taskQueue (task,stage) value ('image26.jpg',1);
insert into taskQueue (task,stage) value ('image27.jpg',1);
insert into taskQueue (task,stage) value ('image28.jpg',1);
insert into taskQueue (task,stage) value ('image29.jpg',1);
insert into taskQueue (task,stage) value ('image30.jpg',1);
insert into taskQueue (task,stage) value ('image31.jpg',1);
insert into taskQueue (task,stage) value ('image32.jpg',1);
insert into taskQueue (task,stage) value ('image33.jpg',1);
insert into taskQueue (task,stage) value ('image34.jpg',1);
insert into taskQueue (task,stage) value ('image35.jpg',1);
insert into taskQueue (task,stage) value ('image36.jpg',1);
insert into taskQueue (task,stage) value ('image37.jpg',1);
insert into taskQueue (task,stage) value ('image38.jpg',1);
insert into taskQueue (task,stage) value ('image39.jpg',1);
insert into taskQueue (task,stage) value ('image40.jpg',1);


    
//also you can put in another stage    
//insert into taskQueue (task,stage) value ('image1.jpg',2);
//insert into taskQueue (task,stage) value ('image2.jpg',2);
//...

//insert into taskQueue (task,stage) value ('image1.jpg',3);
//insert into taskQueue (task,stage) value ('image2.jpg',3);
//...
    

CREATE TABLE lockStatus (
id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
lockState VARCHAR(1) NOT NULL,
stage VARCHAR(2) NOT NuLL,
reg_date TIMESTAMP
);    
    
insert into lockStatus (lockState,stage) value ('y',1);
insert into lockStatus (lockState,stage) value ('y',2);
insert into lockStatus (lockState,stage) value ('y',3);

CREATE TABLE vmadd (
id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
vm VARCHAR(20) NOT NULL,
stage VARCHAR(2),    
reg_date TIMESTAMP
);    
      
insert into vmadd (vm,stage) value ('x',1);
insert into vmadd (vm,stage) value ('x',2);
insert into vmadd (vm,stage) value ('x',3);


CREATE TABLE vmrem (
id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
vm VARCHAR(20) NOT NULL,
stage VARCHAR(2),    
reg_date TIMESTAMP
);    
      
insert into vmrem (vm,stage) value ('x',1);
insert into vmrem (vm,stage) value ('x',2);
insert into vmrem (vm,stage) value ('x',3);
    
    
CREATE TABLE logs (
id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
log VARCHAR(100) NOT NULL,
stage int(6) NOT NULL,
reg_date TIMESTAMP
);    

 
CREATE TABLE wait (
id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
vm VARCHAR(20) NOT NULL,
reg_date TIMESTAMP
);  
    
CREATE TABLE clock (
id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
time int(6) NOT NULL,
qnt int(6) NOT NULL,
cont int(6) NOT NULL,
stage int(6) NOT NULL,
reg_date TIMESTAMP
);  
    
CREATE TABLE result (
id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
mtotal decimal(5,2) NOT NULL,
clock varchar(6) NOT NULL,
vms int(6) NOT NULL,
lastclocktime int(6) NOT NULL,
stage int(6) NOT NULL,
reg_date TIMESTAMP
);    
    

