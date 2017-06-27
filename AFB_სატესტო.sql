/* უბრალო ტესტია, მერე წაიშლება*/
select process_get_id(13, 2), '=2', process_get_param(2, 'invoice_id'), '=13', process_get_param(2, 'step'), '=3';
/* ტესტირდება: docs_invoice_payment_by_paypal(invoiceId, recievedDate, recievedIso, recievedAmount, paypalFee) */
set @ai = (select * from information_schema.tables where TABLE_SCHEMA=schema() and TABLE_NAME = 'docs');
call docs_invoice_payment_by_paypal(invoiceId, recievedDate, recievedIso, recievedAmount, paypalFee);