// Run once when page loads
document.addEventListener("DOMContentLoaded", () => {
    const invoiceInput = document.getElementById("invoiceNumber");

    // When user manually changes invoice
    invoiceInput.addEventListener("change", () => {
        getAllItems();
    });
});


async function searchInvoiceNumber() {
    const typedInvoiceNumber = document.getElementById("invoiceNumber").value;
    const suggestionDiv = document.getElementById("invoiceNumberSuggestions");

    if (!typedInvoiceNumber) {
        suggestionDiv.innerHTML = "";
        return;
    }

    try {
        const response = await fetch(`/SalesReturn/searchInvoiceNumber?id=${encodeURIComponent(typedInvoiceNumber)}`);

        if (!response.ok) {
            throw new Error(`API call failed with status: ${response.status}`);
        }

        const invoiceNumbers = await response.json();
        suggestionDiv.innerHTML = "";

        invoiceNumbers.forEach(inNum => {
            const div = document.createElement("div");
            div.textContent = inNum;

            div.onclick = () => {
                document.getElementById("invoiceNumber").value = inNum;
                suggestionDiv.innerHTML = "";
                getAllItems(); // load items immediately
            };

            suggestionDiv.appendChild(div);
        });

    } catch (error) {
        console.error("Error fetching Invoice Number:", error);
    }
}


async function getAllItems() {
    const selectedInvoiceNumber = document.getElementById("invoiceNumber").value;

    if (!selectedInvoiceNumber) return;

    const response = await fetch(`/SalesReturn/Invoice?id=${encodeURIComponent(selectedInvoiceNumber)}`);

    if (!response.ok) {
        throw new Error(`API call failed with status: ${response.status}`);
    }

    const invoiceDetails = await response.json();
    console.log(invoiceDetails);

    const customerName = invoiceDetails[0].customerName;
    const itemDetails = invoiceDetails;

    const tbody = document.querySelector("#billingTable tbody");
    const templateRow = document.getElementById("templateRow");

    // ✅ Clear old rows
    Array.from(tbody.children).forEach(row => {
        if (row.id !== "templateRow") {
            row.remove();
        }
    });

    // ✅ Set customer name (header level)
    document.getElementById("customerName").textContent =customerName;

    // ✅ Add item rows
    itemDetails.forEach(itemDet => {
        const newRow = templateRow.cloneNode(true);
        newRow.removeAttribute("id");
        newRow.style.display = "";

        newRow.querySelector('input[name="itemCode[]"]').value = "001";
        newRow.querySelector('input[name="itemName[]"]').value = itemDet.itemName;
        newRow.querySelector('input[name="quantity[]"]').value =Number (itemDet.itemQuantity);
        newRow.querySelector('input[name="pricePerUnit[]"]').value = Number(itemDet.itemPrice);
        newRow.querySelector('input[name="productId[]"]').value = Number(itemDet.productId);
        tbody.appendChild(newRow);
    });
}

async function createSalesReturn()
{
    const selectedInvoiceNumber=document.getElementById("invoiceNumber").value
    const customerName = document.getElementById('customerName').textContent;

    console.log("customer ANme is: "+customerName);

    if(!selectedInvoiceNumber)
    {
alert ("Invoice Number cannot be empty")
return;
    }


    const items=[];
    const allRows= document.querySelectorAll("#billingTable tbody tr:not(#templateRow)")

for (const row of allRows) {
    
        const itemName= row.querySelector('input[name="itemName[]"]').value;
        const orderedQuantity= Number( row.querySelector('input[name="quantity[]"]').value);
        const returnedQuantity= Number(row.querySelector('input[name="returnQuantity[]"]').value);

        const itemPrice=Number(row.querySelector('input[name="pricePerUnit[]"]').value);
        const productId=Number(row.querySelector('input[name="productId[]"]').value);

       if(returnedQuantity>orderedQuantity)
        {
            alert('return quanity must be less than ordered quntity');
            return;
        }

    if(returnedQuantity>=1 && returnedQuantity<=orderedQuantity)
    {
        let rowItem={
            itemName,
            orderedQuantity,
            returnedQuantity, 
            itemPrice,
            productId
        }
    items.push(rowItem);
    console.log(rowItem);
    }
    
}

         
    let returnObject={
        invoiceId:selectedInvoiceNumber,
        customerName:customerName,
        items:items,
        tax:10,
        subTotal:100,
        total:1000,
    }

    const response = await fetch(`/SalesReturn/save`,
        {
            method:"post",
            headers:{"content-type":"application/json"},
            body: JSON.stringify(returnObject)
        }
    )

    if(response.ok)
    {
        alert("Return Successfull");
    }
    else{
        alert("Something Went Wrong");
    }
}
