import { useEffect, useState } from "react";
import API from "../services/api";

function ItemList() {
  const [items, setItems] = useState([]);

  useEffect(() => {
    API.get("/items")
      .then((res) => setItems(res.data))
      .catch((err) => console.error(err));
  }, []);


  return (
    <div style={{ padding: "20px" }}>
      <h2>Items List</h2>
      <ul>
        {items.map((item) => (
          <li key={item.id}>{item.name} {item.price}</li>
        ))}
      </ul>

    </div>
  );
}

export default ItemList;
