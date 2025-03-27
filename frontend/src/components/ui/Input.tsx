import React from "react";

interface InputProps {
  type: string;
  placeholder: string;
  className?: string;
  value?: string;
  onChange?: (event: React.ChangeEvent<HTMLInputElement>) => void;
  disabled?: boolean;
}

export function Input({ type, placeholder, className, value, onChange, disabled }: InputProps) {
  return (
    <input
      type={type}
      placeholder={placeholder}
      value={value}
      onChange={onChange}
      disabled={disabled}
      className={`px-4 py-2 border rounded w-full ${className}`}
    />
  );
};
