INSERT INTO USER (User_UUID, User_Name, Password, Email_Address, Preferred_Phone_Number) VALUES ('200b8039-5023-43aa-a9fc-253612bd9b01', 'Mark', 'admin', 'mark@test.com', '087087087');
INSERT INTO USER (User_UUID, User_Name, Password, Email_Address, Preferred_Phone_Number) VALUES ('200b8039-5023-43aa-a9fc-253612bd9b05', 'Mary', 'admin', 'mary@test.com', null);
INSERT INTO USER (User_UUID, User_Name, Password, Email_Address, Preferred_Phone_Number) VALUES ('200b8039-5023-43aa-a9fc-253612bd9b02', 'Tom', 'admin', 'tom@test.com', '081081081');
INSERT INTO USER (User_UUID, User_Name, Password, Email_Address, Preferred_Phone_Number) VALUES ('200b8039-5023-43aa-a9fc-253612bd9b03', 'John', 'admin', 'john@test.com', '082082082');
INSERT INTO USER (User_UUID, User_Name, Password, Email_Address, Preferred_Phone_Number) VALUES ('200b8039-5023-43aa-a9fc-253612bd9b04', 'Lisa', 'admin', 'lisa@test.com', '083083083');

INSERT INTO PHONES (Phone_UUID, Phone_Name, Phone_Number, Phone_Model, User_UUID) VALUES ('100b8039-5023-43aa-a9fc-253612bd9b01', 'Mark iPhone', '087087087', 'IPHONE', '200b8039-5023-43aa-a9fc-253612bd9b01');
INSERT INTO PHONES (Phone_UUID, Phone_Name, Phone_Number, Phone_Model, User_UUID) VALUES ('100b8039-5023-43aa-a9fc-253612bd9b02', 'Mark Android', '010101011', 'ANDROID', '200b8039-5023-43aa-a9fc-253612bd9b01');
INSERT INTO PHONES (Phone_UUID, Phone_Name, Phone_Number, Phone_Model, User_UUID) VALUES ('100b8039-5023-43aa-a9fc-253612bd9b03', 'Mark Desk', '020202020', 'DESK_PHONE', '200b8039-5023-43aa-a9fc-253612bd9b01');
INSERT INTO PHONES (Phone_UUID, Phone_Name, Phone_Number, Phone_Model, User_UUID) VALUES ('100b8039-5023-43aa-a9fc-253612bd9b04', 'Tom iPhone', '030303030', 'IPHONE', '200b8039-5023-43aa-a9fc-253612bd9b02');
INSERT INTO PHONES (Phone_UUID, Phone_Name, Phone_Number, Phone_Model, User_UUID) VALUES ('100b8039-5023-43aa-a9fc-253612bd9b05', 'Tom Android', '081081081', 'ANDROID', '200b8039-5023-43aa-a9fc-253612bd9b02');
INSERT INTO PHONES (Phone_UUID, Phone_Name, Phone_Number, Phone_Model, User_UUID) VALUES ('100b8039-5023-43aa-a9fc-253612bd9b06', 'John iPhone', '082082082', 'IPHONE', '200b8039-5023-43aa-a9fc-253612bd9b03');
INSERT INTO PHONES (Phone_UUID, Phone_Name, Phone_Number, Phone_Model, User_UUID) VALUES ('100b8039-5023-43aa-a9fc-253612bd9b07', 'John Soft phone', '0404040404', 'SOFT_PHONE', '200b8039-5023-43aa-a9fc-253612bd9b03');
INSERT INTO PHONES (Phone_UUID, Phone_Name, Phone_Number, Phone_Model, User_UUID) VALUES ('100b8039-5023-43aa-a9fc-253612bd9b08', 'Lisa Android', '083083083', 'ANDROID', '200b8039-5023-43aa-a9fc-253612bd9b04');

