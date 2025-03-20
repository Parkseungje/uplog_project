export function Input({ type, placeholder, className, value, onChange }) {
  return (
    <input
      type={type}
      placeholder={placeholder}
      value={value}  // ✅ 상태 반영
      onChange={onChange}  // ✅ 이벤트 전달
      className={`px-4 py-2 border rounded w-full ${className}`}
    />
  );
}
