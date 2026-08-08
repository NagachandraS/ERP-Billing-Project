# ERP-Billing-Project
ERP project which helps user to run their business easy and efficient way

**Features:**
1. Add, Edit and view customers
2. Add, Edit and view Vendors
3. Add, Edit and view Products
4. Create Sales Invoice
5. Create Purchase Invoice
6. Create Sales Return Invoice
7. Create Purchase Return Invoice
8. Maintain the Inventory
9. Good usefull 5 reports

**Current Phase : **Testing in progress and post that needs to fix all of them

**Next Phase:** Impementing Tally (Ledger Entry)

**SAMPLE SCREENSHOTS**

Inventory Screenshot:
<img width="1896" height="826" alt="image" src="https://github.com/user-attachments/assets/40576e56-d900-45b8-a6fb-eaca5285e4d4" />


Okay where do we Add product? To answer this question we have the Add product screen where user can add all products and enters the initial stock.
<img width="746" height="832" alt="image" src="https://github.com/user-attachments/assets/a21ab08d-628f-44df-9ae5-f19f2651cceb" />

Now lets go to our main feature, creating an sales invoice. Here we can select our customer name, products which are available in stocks. By just clicking the save invoice.
It Updates the stock
It affects the sales report
It updates the customer billing amout etc....
<img width="1901" height="503" alt="image" src="https://github.com/user-attachments/assets/558a40e0-898b-4855-9c5a-39983f74e972" />


**Searching the customer by customer Name:**
When user start typing the required customer name, the existing customer name starts populating based on the characters entere by the user

**Seaching the product Name:** 
User can search and select the product by
1. Product Name
2. Barcode (yet to be implemeneted)
3 Product Code (yet to be implemeneted)

**Enetering the quantity: **
User can enetred the quanity and the calculation automatically updates to each row. 
There is a master table where we have the data of price, available quantity etc. using that calculation auto updates in each of the row in the item grid

**Automatic total Calculation:**
Total also autocalculates and displays (each item row total + GST )
Current GST is hardcoded to 10% of the subtotal (Dymanic GST calculation yet to be implemented)

<img width="1912" height="631" alt="image" src="https://github.com/user-attachments/assets/9953ee1c-c2d5-4d3b-9a69-60aec00ea65b" />
Once the invoice creation is successfull the Inventory gets updated with the latest quantity for the sold items/products
(Sales: the quantity gets reduced)

**Negative Cases for Invoice creation: **
User cannot create the invoice with the hight quantity than the available stiock

**Sales Return: **
User can also perform and record the return scenario that he may encounter with his business

Steps: 
1. Search and select the invoice
2. Auto populates the data of sold product
3. Enter the returned quantity
4. Confirm
<img width="1900" height="602" alt="image" src="https://github.com/user-attachments/assets/3fa2e51d-27f8-4805-afa4-13220a19a839" />


After the return the quantity updates in the invetory for the returned quantity
(Sales Return: quantity gets added)

Purchase: Same scenarios handled for the purchase business flow

