const Header = () => {
  return (
    <div className="bg-dark" style={{ marginLeft: "-12px" }}>
      <div className="row">
        <div className="col">
          <button
            style={{ marginLeft: "200px" }}
            className="mt-2 mb-2 btn btn-primary"
          >
            Add new
          </button>
        </div>

        <div className="col">
          <span className="float-end me-5 mt-3 text-light">
            <h4>Login &nbsp;&nbsp; Logout</h4>
          </span>
        </div>
      </div>
    </div>
  );
};

export default Header;
