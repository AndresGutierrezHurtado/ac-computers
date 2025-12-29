const getData = async (endpoint, method, data = {}) => {
    const response = await fetch(process.env.NEXT_PUBLIC_API_URL + endpoint, {
        headers: {
            "Content-Type": "application/json",
            Accept: "application/json",
        },
        method,
        body: JSON.stringify(data),
    });

    return await response.json();
};

const useGetData = async (endpoint) => {
    return await getData(endpoint, "GET");
};

const usePostData = async (endpoint, data) => {
    return await getData(endpoint, "POST", data);
};

const usePutData = async (endpoint, data) => {
    return await getData(endpoint, "PUT", data);
};

const useDeleteData = async (endpoint) => {
    return await getData(endpoint, "DELETE");
};
