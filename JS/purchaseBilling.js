    function fileHandle(input)
    {
        const fileName = input.files.length > 0
            ? input.files[0].name
            : "No File Selected";

        document.getElementById("fileName").textContent = fileName;

        const file = input.files[0];

        if (!file)
        {
            alert("No file selected");
            return;
        }

        const formData = new FormData();

        formData.append("pdfFile", file);

        fetch("/purchaseOrder/uploadFile",
        {
            method: "POST",
            body: formData
        })

        // Backend now returns JSON
        .then(response => response.json())

        .then(data =>
        {
            console.log("success", data);

            // Remove existing rows except template row
            document.querySelectorAll("#purchaseTable tbody tr:not(#templateRow)")
                .forEach(row => row.remove());

            // Loop through parsed items
            data.forEach(item =>
            {
                // Add new row
                addRow();

                // Get latest added row
                const rows = document.querySelectorAll("#purchaseTable tbody tr:not(#templateRow)");
                const latestRow = rows[rows.length - 1];

                // Fill values
                latestRow.querySelector('input[name="itemName[]"]').value = item.itemName;
                latestRow.querySelector('input[name="itemCode[]"]').value=item.itemCode;
                latestRow.querySelector('input[name="quantity[]"]').value = item.itemPurchaseQuantity;
                latestRow.querySelector('input[name="pricePerUnit[]"]').value = item.itemPurchasePrice;
                latestRow.querySelector('input[name="productId[]"]').value = item.productId;

                // Update row total
                updateRowTotal(
                    latestRow.querySelector('input[name="quantity[]"]')
                );
            });

            alert("PDF parsed successfully");
        })

        .catch(error =>
        {
            console.error("Error", error);
            alert("File upload failed");
        });
    }
async function searchVendor() {

    const typedData= document.getElementById("vendorName").value
    const suggestionDiv= document.getElementById("vendorSuggestions")

    if (!typedData)
    {
        suggestionDiv.innerHTML="";
        return;
    }

    try
    {
        const response = await fetch (`/vendor/search?name=${encodeURIComponent(typedData)}`)

        if (!response.ok)
        {
            throw new Error(`API call failed with status: ${response.status}`);
        }

       const vendors= await response.json();
       suggestionDiv.innerHTML="";

       vendors.forEach(vend=>{
        const div= document.createElement("div")
        div.textContent =vend.vendorName;

        div.onclick= ()=>{
            document.getElementById("vendorName").value=vend.vendorName;
            suggestionDiv.innerHTML = "";
        }
        suggestionDiv.appendChild(div)

       });

    }
    catch(error)
    {
 console.error("Error fetching vendors:", error);
    }
    
}

async function searchProducts(input) {

    const typedData= input.value
    const suggestionDiv = input.parentNode.querySelector(".suggestions");
    
    if(!typedData)
    {
        suggestionDiv.innerHTML="";
        return;
    }
    try{
        const response = await fetch (`/api/products/search?name=${encodeURIComponent(typedData)}`)

        if(!response.ok)
        {
            throw new Error(`API call failed with status: ${response.status}`)
        }
        const products =await response.json();
        products.forEach(prod=>
        {
          const div=  document.createElement("div");
          div.textContent=prod.productName;

          div.onclick= () =>
          { 
                const row = input.closest('tr');
                row.querySelector('input[name="itemName[]"]').value=prod.productName;
                //row.querySelector('input[name="pricePerUnit[]"]').value=prod.productPrice;
                row.querySelector('input[name="productId[]"]').value=prod.productId;
                suggestionDiv.innerHTML = "";
          };
             suggestionDiv.appendChild(div);
        })
    }
    catch(error)
    {
        console.error("fetching products failed",error)
    }
}

 function addRow() {
    const tableBodyCopy = document.querySelector("#purchaseTable tbody");
    const trCopy=document.getElementById("templateRow");

    //clone the row
    const newRow= trCopy.cloneNode(true);
    newRow.style.display="";
    newRow.removeAttribute("id");
    newRow.removeAttribute("name")

    newRow.querySelectorAll("input").forEach(input=>
    {
input.value="";
    })
    newRow.querySelectorAll(".suggestions").forEach(suggestion=>
    {
suggestion.innerHTML="";
    }
    )
    newRow.querySelector('input[name="itemName[]"]').onkeyup=function(){
        searchProducts(this);
    }
    newRow.querySelector('input[name="quantity[]"]').oninput = function () {
    updateRowTotal(this);
    }
    newRow.querySelector('button[class="delete-row-btn"]').onclick=function()
    {
        deleteRow(this);
    }
tableBodyCopy.appendChild(newRow);
    
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

function updateRowTotal(input)
{
     console.log("Received:", input);
if(!input instanceof HTMLElement)
{
console.error("check", input)
    return;
}

    const row =input.closest('tr');

     
    const typedQuantity = parseFloat (row.querySelector('input[name="quantity[]"]').value)||0;
    const typedPrice= parseFloat(row.querySelector('input[name="pricePerUnit[]"]').value)||0;
    const totalField= row.querySelector('input[name="total[]"]');

     totalField.value =(typedQuantity*typedPrice).toFixed(2);
     
calculateTotals();
}

function calculateTotals()

{
    const totalRows= document.querySelectorAll('input[name="total[]"]')
     let subtotal = 0;

    totalRows.forEach(tr => {
        subtotal += parseFloat(tr.value)||0; // Safely handle empty inputs
    });

    const gst=subtotal*0.10;
     const grandTotal = subtotal + gst;

    document.getElementById('subtotal').textContent=subtotal.toFixed(2);
    document.getElementById('gst').textContent=gst.toFixed(2);
    document.getElementById('grandTotal').textContent=grandTotal.toFixed(2);
}

async function generateInvoice() {
    const userInputOnVendor= document.getElementById("vendorName");
    const selectedVendor=userInputOnVendor.value.trim();

    if(!selectedVendor)
    {
        alert('Vendor Cannot be Empty');
        return;
    }
    const items=[];
    
    const rows= document.querySelectorAll("#purchaseTable tbody tr:not(#templateRow)")
    rows.forEach(row=>{
        const itemName=row.querySelector('input[name="itemName[]"]').value.trim();
        const itemPurchaseQuantity=Number (row.querySelector('input[name="quantity[]"]').value);
        const itemPurchasePrice= Number(row.querySelector('input[name="pricePerUnit[]"]').value);
        const productId=row.querySelector('input[name="productId[]"]').value;

        if (itemName !== "")
        {
        let rowItem ={
            itemName,
            itemPurchaseQuantity,
            itemPurchasePrice,
            productId
        }
        items.push(rowItem);
        console.log(rowItem);
        }
    }) 
    
    if (items.length===0)
    {
        alert("please add atleast one product")
        return;
    }

    const subtotal=Number(document.getElementById("subtotal").textContent);
    const gst=Number(document.getElementById("gst").textContent);
    const grandTotal=Number(document.getElementById("grandTotal").textContent);

    for(let i=0; i<items.length;i++)
    {
        if(items[i].itemQuantity <=0)
        {
            alert("quantity must be greater than 0")
            return;
        }
        if(items[i].itemPrice<=0)
        {
            alert("Price must be greater than 0")
            return;
        }
    }

    let billingObject ={
        vendorName: selectedVendor,
        items:items,
        subtotal:subtotal,
        gst:gst,
        grandTotal:grandTotal
    }

    const response = await fetch("/purchaseOrder/save" ,
        {
            method:"post",
            headers:{"content-type":"application/json"},
            body: JSON.stringify(billingObject)
        }
    )

    if (response.ok)
    {
        window.location.reload();
        alert("Invoice saved successfully")
    }
    else{
        alert("something went wrong")
    }


}
