async function searchPurchaseOrderNumber(){

const typedData = document.getElementById("invoiceNumber").value
const suggestionDiv= document.getElementById("invoiceNumberSuggestions");

if(!typedData)
{
suggestionDiv.innerHTML ="";
return
}

try{

const response = await fetch (`/PurchaseReturn/searchInvoiceNumber?id=${encodeURIComponent(typedData)}`);
console.log(response);
if (!response.ok)
{
throw new Error(`API call failed with status: ${response.status}`);
}

const purchaseOrderNumbers= await response.json();
console.log(purchaseOrderNumbers);
suggestionDiv.innerHTML = "";


purchaseOrderNumbers.forEach(orderNumbers =>
{
            const div = document.createElement("div");
            div.textContent = orderNumbers;

div.onClick=()=>
{
document.getElementById("invoiceNumber").value = orderNumbers;
suggestionDiv.innerHTML = "";
};

suggestionDiv.appendChild(div);
});

}
catch(error)
{
console.error("Error fetching Invoice Number:", error);
}



}