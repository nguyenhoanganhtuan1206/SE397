use se_397;

drop trigger insert_account_after_insert_user;
use se_397;
delimiter //
create trigger insert_account_after_insert_user
after insert 
on customer for each row
begin
	insert into `account` (username , `password` , customer_id)
    values(NEW.email , '123' , NEW.id);
end //
delimiter ;

select * from customer where email = "anhtuan@gmail.com";

select * from `account` where `account`.username = "nguyentuan@gmail.com" and `password` = "abc123";

insert into product values(1,'/assets/img/product/product-1.jpg' , 'Surface Pro 8' , 899 ,5, 1 , 1 , 1);
insert into product values(2,'/assets/img/product/product-2.jpg' , 'Screen Gaming LG' , 340.99 ,5 , 1 , 4 , 1);
insert into product values(3,'/assets/img/product/product-3.jpg' , 'Surface Laptop 4' , 700,5 , 1 , 1 , 1);
insert into product values(4,'/assets/img/product/product-4.jpg' , 'Surface Studio' , 750 ,5, 1 , 2 , 1);
insert into product values(5,'/assets/img/product/product-5.jpg' , 'Surface Laptop Studio' , 895.99 ,5, 1 , 1 , 1);
insert into product values(6,'/assets/img/product/product-6.jpg' , 'Macbook Pro' , 850.9 ,5, 1 , 1 , 1);
insert into product values(7,'/assets/img/product/product-7.jpg' , 'Wireless Headphones' , 350,5 , 1 , 3 , 1);
insert into product values(8,'/assets/img/product/product-8.jpg' , 'Mac Book Air 2021' , 898.99,5 , 1 , 1 , 1);

INSERT INTO `se_397`.`product` (`image`, `name`, `price`,`quantity`, `status`, `category_id`) VALUES ('/assets/img/product/product-17.jpg', 'M75 Sport Watch', '250.00',5, b'1', 4);

insert into category values(1,'Laptop');
insert into category values(2,'Screen');
insert into category values(3,'Headphone');
insert into category values(4,'Watches');

INSERT INTO `se_397`.`promotiondetail` (`end_date`, `start_date`, `status`, `title`) VALUES ('2021-03-18', '2021-03-08', 1, 'Women Day');

INSERT INTO `se_397`.`product` (`image`, `name`, `price`, `quantity`, `status`, `category_id`) VALUES ('/assets/img/product/product-13.jpg', 'Macbook Pro 2021', '870.99', '5', b'1', b'1');

select * from orders where user_id = 3;

select * from `user` 
inner join user_role
on `user`.id = user_role.user_id 
inner join `role`
on `role`.id = user_role.user_id
where `role`.id = 2;

UPDATE `se_397`.`orders` SET `status` = 0 WHERE (`id` = '1');

/* Tinh tong tien khi mua*/
select sum(orderdetail.quantity * product.price) from product 
inner join orderdetail 
on orderdetail.product_id = product.id
inner join orders
on orders.id = orderdetail.order_id
group by orders.id;

select `user`.`name` , orders.start_date ,  sum(orderdetail.quantity * product.price) , `user`.address , `user`.phoneNumber , orders.`status` , province.`name` from product  inner join orderdetail on orderdetail.product_id = product.id inner join orders
on orderdetail.order_id = orders.id
inner join `user`
on `user`.id = orders.user_id
inner join province
on province.id = `user`.province_id
group by user_id;

select * from `user` inner join user_role on user_role.user_id = `user`.id inner join `role` on `role`.id = user_role.role_id where `role`.id = 2;


select * from orders order by  orders.`status` , orders.start_date desc;

select * from orders 
inner join `user`
on orders.user_id = `user`.id
where `user`.`name` like concat('%' , "Tuấn" , '%');

select * from orderdetail 
inner join orders
on orders.id = orderdetail.order_id
where month(orders.start_date) = month(CURDATE());

select * from orderdetail 
inner join orders
on orders.id = orderdetail.order_id
where year(orders.start_date) = year(CURDATE());

select * from `user` where `user`.id = 2;

select * from orders where user_id = 3 order by orders.status asc , orders.id desc;

select * from product where product.`name` like concat('%', '' ,'%')
order by `status` desc;

select percent from promotion;

/* Xoa nhieu bang cung voi user */
DELETE from users where id = 6;

drop trigger delete_tables_involved_user;
delimiter #
create trigger delete_tables_involved_user
before delete 
on users for each row
begin
		delete from evaluation where evaluation.user_id = OLD.id;
		delete from orders where orders.user_id = OLD.id;
		delete from user_role where user_role.user_id = OLD.id;
END#
delimiter ;


drop trigger delete_tables_involved_orders;
delimiter //
create trigger delete_tables_involved_orders
before delete 
on orders for each row
begin
		delete from orderdetail where orderdetail.order_id = OLD.id;
end //
delimiter ;




