import useFetch from "./useFetch";

const Table = () => {
  const {
    error,
    isPending,
    data: certificates,
  } = useFetch("http://localhost:8080/certificates");

  console.log(certificates);

  return (
    <table className="table table-dark table-hover ms-5 mt-5">
      {error && <div>{error}</div>}
      {isPending && <div>Loading...</div>}
      {certificates && (
        <div className="new">
          <thead>
            <tr style={{ textAlign: "center" }}>
              <th>ID</th>
              <th>Name</th>
              <th>Duration</th>
              <th>Price</th>
              <th>Creation date</th>
              <th>Action</th>
              <th>Action</th>
              <th>Action</th>
            </tr>
          </thead>
          {certificates.map((certificate) => (
            <tr key={certificate.giftCertificateId}>
              <td>{certificate.giftCertificateId}</td>
              <td>{certificate.certificateName}</td>
              <td>{certificate.duration}</td>
              <td>{certificate.price}</td>
              <td>{certificate.creationDate}</td>
              <td>
                <button className="btn btn-success">View</button>
              </td>
              <td>
                <button className="btn btn-warning">Edit</button>
              </td>
              <td>
                <button className="btn btn-danger">Delete</button>
              </td>
            </tr>
          ))}
        </div>
      )}
    </table>
  );
};

export default Table;
