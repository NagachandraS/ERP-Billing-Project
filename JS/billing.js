async function searchCustomers() {
    const typedData = document.getElementById("customerName").value;
    const suggestionDiv = document.getElementById("customerSuggestions");

    if (!typedData) {
        suggestionDiv.innerHTML = "";
        return;
    }

    try {
        const response = await fetch(`/api/customers/search?name=${encodeURIComponent(typedData)}`);

        if (!response.ok) {
            throw new Error(`API call failed with status: ${response.status}`);
        }

        const customers = await response.json();

        suggestionDiv.innerHTML = "";
        customers.forEach(cust => {
            const div = document.createElement("div");
            div.textContent = cust.customerName;
            div.onclick = () => {
                document.getElementById("customerName").value = cust.customerName;
                suggestionDiv.innerHTML = "";
            };
            suggestionDiv.appendChild(div);
        });
    } catch (error) {
        console.error("Error fetching customers:", error);
    }
}


async function searchProductByBarCode(input)
{
    const typedData = input.value.trim();
    if(!typedData)
    {
        return;
    }
    try
    {
        const response = await fetch(`/api/products/barCode/${encodeURIComponent(typedData)}` );
        if(!response.ok)
        {
            throw new Error(`API failed: ${response.status}`);
        }
        const product = await response.json();
        // Product not found
        if(!product || !product.productId)
        {
            alert("Product not found");
            const row=input.closest('tr');
            row.querySelector('input[name="itemName[]"]').value="";
            row.querySelector('input[name="pricePerUnit[]"]').value="";
            row.querySelector('input[name="productId[]"]').value="";
            return;
        }
        const currentRow=input.closest('tr');

        // ===== Duplicate Product Check =====
        const allRows=document.querySelectorAll("#billingTable tbody tr:not(#templateRow)");

        for(let row of allRows)
        {
            // Skip current row
            if(row===currentRow)
            {
                continue;
            }
            const existingProductId= row.querySelector('input[name="productId[]"]').value;

            if(existingProductId==product.productId)
            {
                const quantityInput=
                row.querySelector('input[name="quantity[]"]');

                let quantity=
                parseFloat(quantityInput.value)||0;
                quantity++;
                quantityInput.value=quantity;
                updateRowTotal(quantityInput);
                currentRow.remove();
                return;
            }
        }
        // ===== Populate current row =====
        currentRow.querySelector('input[name="itemName[]"]').value=product.productName;
        currentRow.querySelector('input[name="pricePerUnit[]"]').value=product.productPrice;
        currentRow.querySelector('input[name="itemBarCode[]"]').value=product.barCode;
        currentRow.querySelector('input[name="productId[]"]').value=product.productId;
        // Default quantity
        currentRow.querySelector('input[name="quantity[]"]' ).value=1;
        updateRowTotal(currentRow.querySelector('input[name="quantity[]"]'));
        // Cursor automatically goes to quantity
        currentRow.querySelector('input[name="quantity[]"]').focus();
        // Create next empty row automatically
        const rows=document.querySelectorAll("#billingTable tbody tr:not(#templateRow)");
        const lastRow=rows[rows.length-1];
        if(currentRow===lastRow)
        {
            addRow();
        }

    }
    catch(error)
    {
        console.error("Barcode search failed:",error);
        alert("Barcode lookup failed");
    }
}

async function searchProducts(input) {

    const typedData = input.value;
    const suggestionDiv = input.parentNode.querySelector(".suggestions");

    if (!typedData) {
        suggestionDiv.innerHTML = "";
        return;
    }

    try {
        const response = await fetch(`/api/products/search?name=${encodeURIComponent(typedData)}`);

        if (!response.ok) {
            throw new Error(`API call failed with status: ${response.status}`);
        }

        const products = await response.json();
        suggestionDiv.innerHTML = "";

        products.forEach(prod => {
            const div = document.createElement("div"); // Corrected: `CreateElement` to `createElement`
            div.textContent = prod.productName;

            div.onclick = () => {
                const row = input.closest('tr');
                row.querySelector('input[name="itemName[]"]').value=prod.productName;
                row.querySelector('input[name="pricePerUnit[]"]').value=prod.productPrice;
                row.querySelector('input[name="productId[]"]').value=prod.productId;
                suggestionDiv.innerHTML = "";
            };
            suggestionDiv.appendChild(div);
        });
    }
    catch (error) {
        console.error("Error fetching products:", error);
    }
}

function updateRowTotal(quantityInput)
{
const row = quantityInput.closest('tr')
const quantity= parseFloat(quantityInput.value) ||0
const pricePerUnit = parseFloat(row.querySelector('input[name="pricePerUnit[]"]').value) || 0;
const rowTotal= row.querySelector('input[name="total[]"]');
console.log("Quantity:", quantityInput.value, "Price:", row.querySelector('input[name="pricePerUnit[]"]').value);
const rowTotalResult= quantity*pricePerUnit;
rowTotal.value = rowTotalResult.toFixed(2);

calculateTotals();
}

function addRow() {
  const tableBody = document.querySelector("#billingTable tbody");
  const template = document.getElementById("templateRow");

  // Clone the template row
  const newRow = template.cloneNode(true);
  newRow.style.display = ""; // make it visible
  newRow.removeAttribute("id"); // prevent duplicate IDs

  // Clear values from inputs
  newRow.querySelectorAll("input").forEach(input => input.value = "");

  // Clear suggestions (autocomplete dropdowns)
  newRow.querySelectorAll(".suggestions").forEach(div => div.innerHTML = "");

  // Reattach event listeners
  newRow.querySelector('input[name="itemName[]"]').onkeyup = function () {
    searchProducts(this);
  };
  newRow.querySelector('input[name="quantity[]"]').oninput = function () {
    updateRowTotal(this);
  };
  newRow.querySelector('button').onclick = function () {
    deleteRow(this);
  };

  // Append new row to table body
  tableBody.appendChild(newRow);
}


function deleteRow(button) {
  const row = button.closest("tr");
  row.remove();

  // If all rows deleted → automatically add one empty row
  const tableBody = document.querySelector("#billingTable tbody");
  if (tableBody.querySelectorAll("tr:not(#templateRow)").length === 0) {
    addRow();
  }

  // Recalculate totals (optional if you have totals below)
  if (typeof calculateTotals === "function") {
    calculateTotals();
  }
}

window.addEventListener("load", function() {
    // Add initial row
    addRow();

    // Attach listener for Generate Invoice button
    const generateButton = document.getElementById("generateBtn");
   // if (generateButton) {
      //  console.log("Generate button listener attached");
        generateButton.addEventListener("click", function() {
          //  console.log("Generate button clicked!");
            generateInvoice();
        });
   // } else {
      //  console.error("Generate button not found!");
    });
//});

function calculateTotals() {
    const totalRows = document.querySelectorAll('input[name="total[]"]');
    let subtotal = 0;

    totalRows.forEach(input => {
        subtotal += parseFloat(input.value)||0; // Safely handle empty inputs
    });

    // Calculate GST (10%)
    const gst = subtotal * 0.10;

    // Calculate Grand Total
    const grandTotal = subtotal + gst;

    // Update DOM with 2 decimal places
    document.getElementById('subtotal').textContent = subtotal.toFixed(2);
    document.getElementById('gst').textContent = gst.toFixed(2);
    document.getElementById('grandTotal').textContent = grandTotal.toFixed(2);
}


async function generateInvoice()
{
console.log("Invoice function called");
const customerInput= document.getElementById("customerName");
const customerName=customerInput.value.trim();
 customerInput.style.boxShadow;
if (!customerName)
{
  alert("Customer Name should not be blank")
  return;
}

const subtotal= document.getElementById("subtotal").textContent;
const gst= document.getElementById("gst").textContent;
const grandTotal= document.getElementById("grandTotal").textContent;
const items=[];
const rows = document.querySelectorAll("#billingTable tbody tr:not(#templateRow)");

rows.forEach(row =>{
const itemName= row.querySelector('input[name="itemName[]"]').value;
const itemQuantity= row.querySelector('input[name="quantity[]"]').value;
const itemPrice=row.querySelector('input[name="pricePerUnit[]"]').value;
const productId= row.querySelector('input[name="productId[]"]').value;

  if (itemName!=="")
  {
    let rowItem = {
      itemName,
      itemQuantity,
      itemPrice,
      productId
    }
    items.push(rowItem);
    return;
  }

}
)
if(items.length===0)
{
  alert("Please add at least one product");
  return;
}

for (let i = 0; i < items.length; i++) {

    if (!items[i].itemQuantity || Number(items[i].itemQuantity) <= 0) {
        alert("Quantity must be greater than 0");
        return;
    }

}

let  billingObject ={
     customerName:customerName,
     items:items,
     subtotal:subtotal,
     gst:gst,
     grandTotal: grandTotal
  
}
//console.log(JSON.stringify(billingObject, null, 2));

const response = await fetch("/billing/save", 
  {
    method:"POST",
    headers: {"content-type": "application/json"},
    body:JSON.stringify(billingObject)
  })
  if (response.ok){
  window.location.reload();
alert("Invoice Saved Succesfuly");
  }
  else {
alert("something went wrong");
  }
}

