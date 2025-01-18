import React, {createContext, useCallback, useEffect, useMemo, useState} from "react";

export const CartContext = createContext({
    numberCartItems: 0,
    fetchNumberCartItems: () => {
    }
});

export function CartProvider({children}: React.PropsWithChildren<{}>) {
    const [numberCartItems, setNumberCartItems] = useState(0);
    useEffect(() => {
        fetchNumberCartItems();
    }, []);
    const fetchNumberCartItems = useCallback(() => {
        CartService.getNumberCartItems()
            .then((numberCartItems) => setNumberCartItems(numberCartItems))
            .catch((err) => console.log(err));
    }, [])

    const stateCartContext = useMemo(() => (
            {
                numberCartItems,
                fetchNumberCartItems
            }
        ),[numberCartItems,fetchNumberCartItems]
    );
    return <CartContext value={stateCartContext}>{children}</CartContext>

}