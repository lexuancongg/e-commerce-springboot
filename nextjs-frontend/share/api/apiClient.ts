interface RequestOptions{
    method: string ;
    headers:{
        [key:string] :string
    };
    body?: any;
}

const callApi = async (method : string , endPoint:string , data:any = null, contentType : string| null =null):Promise<Response> =>{
    const contentTypeDefault: string = 'application/json; charset=UTF-8';
    const requestOptions : RequestOptions = {
        method : method.toUpperCase(),
        headers:{
            'Content-type': contentType ?? contentTypeDefault,
        },

    }
    if(data){
        if(data instanceof FormData){
            delete requestOptions.headers['Content-type']
        }
        requestOptions.body = data;
    }
    try {
        return await fetch(endPoint, requestOptions.method == 'GET' ? undefined : requestOptions);
    }catch (err){
        throw err;
    }
}

const apiClient = {
    get : (endPoint:string) => callApi('GET',endPoint),
    post : (endPoint:string , data : any = null , contentType : string | null = null)=> callApi('POST',endPoint,data,contentType),
    put : (endPoint : string , data : any = null, contentType: string | null = null) =>callApi('PUT',endPoint,data,contentType),
    delete : (endPoint:string ) => callApi('DELETE',endPoint)
}

export default  apiClient;