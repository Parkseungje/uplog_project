export function Button({ children, onClick, variant = "default" }) {
    const baseStyle = "px-4 py-2 rounded text-white";
    const variantStyles = {
      default: "bg-blue-500 hover:bg-blue-600",
      outline: "border border-blue-500 text-blue-500 hover:bg-blue-500 hover:text-white",
    };
    
    return (
      <button className={`${baseStyle} ${variantStyles[variant]}`} onClick={onClick}>
        {children}
      </button>
    );
  }
  